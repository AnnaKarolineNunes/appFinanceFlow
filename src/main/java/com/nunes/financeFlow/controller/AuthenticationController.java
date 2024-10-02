package com.nunes.financeFlow.controller;


import com.nunes.financeFlow.infraSecurity.TokenService;
import com.nunes.financeFlow.models.user.LoginResponseDTO;
import com.nunes.financeFlow.models.user.RegisterDTO;
import com.nunes.financeFlow.models.user.Usuario;
import com.nunes.financeFlow.models.user.AuthenticationDTO;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        // Autenticando usando email e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        // Verifica se já existe um usuário com esse email
        if(this.usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        // Criptografando a senha antes de salvar
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario newUser = new Usuario();
        newUser.setEmail(data.email());
        newUser.setSenha(encryptedPassword);
        newUser.setNome(data.nome());  // Assumindo que a DTO agora tenha o campo nome

        this.usuarioRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
