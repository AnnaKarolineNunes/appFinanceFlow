package com.nunes.financeFlow.controller;

import com.nunes.financeFlow.infraSecurity.TokenService;
import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.user.*;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import com.nunes.financeFlow.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (Usuario) auth.getPrincipal();

        // Verifica se o e-mail foi verificado
        if (!user.isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponseDTO("E-mail não verificado. Por favor, verifique seu e-mail."));
        }

        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já registrado.");
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());
        Usuario newUser = new Usuario();
        newUser.setEmail(data.email());
        newUser.setSenha(encryptedPassword);
        newUser.setNome(data.nome());
        newUser.setRole(data.role() != null ? data.role() : UserRole.USER);

        Usuario savedUser = this.usuarioRepository.save(newUser);

        Conta newConta = new Conta();
        newConta.setUsuario(savedUser);
        savedUser.setConta(newConta);
        this.contaRepository.save(newConta);

        // Gera o token para verificação de e-mail (JWT)
        String verificationToken = tokenService.generateEmailVerificationToken(savedUser);

        // Envia o e-mail de verificação
        try {
            emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuário registrado, mas falha ao enviar e-mail de verificação.");
        }

        return ResponseEntity.ok("Usuário registrado com sucesso. Verifique seu e-mail.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String email = tokenService.validateToken(token);
        if (email == null) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.isEmailVerified()) {
            return ResponseEntity.badRequest().body("E-mail já foi verificado.");
        }

        // Marca o e-mail do usuário como verificado
        usuario.markEmailAsVerified();
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("E-mail verificado com sucesso.");
    }

    @GetMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam("email") String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.isEmailVerified()) {
            return ResponseEntity.badRequest().body("O e-mail já foi verificado.");
        }

        // Gera um novo token de verificação (JWT)
        String verificationToken = tokenService.generateEmailVerificationToken(usuario);

        try {
            emailService.sendVerificationEmail(usuario.getEmail(), verificationToken);
            return ResponseEntity.ok("Novo e-mail de verificação enviado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar novo e-mail de verificação.");
        }
    }
}
