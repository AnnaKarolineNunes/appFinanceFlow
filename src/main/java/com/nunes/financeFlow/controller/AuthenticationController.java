package com.nunes.financeFlow.controller;

import com.nunes.financeFlow.infraSecurity.TokenService;
import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.user.*;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (Usuario) auth.getPrincipal();
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

        // Define o role apenas se foi especificado, caso contrário, use o valor padrão USER
        if (data.role() != null) {
            newUser.setRole(data.role());
        } else {
            newUser.setRole(UserRole.USER);
        }

        Usuario savedUser = this.usuarioRepository.save(newUser);

        Conta newConta = new Conta();
        newConta.setUsuario(savedUser);
        savedUser.setConta(newConta);
        this.contaRepository.save(newConta);

        return ResponseEntity.ok("Usuário e conta registrados com sucesso.");
    }

}
