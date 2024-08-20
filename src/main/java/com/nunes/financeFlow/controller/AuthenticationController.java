package com.nunes.financeFlow.controller;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.nunes.financeFlow.models.dtos.AuthDto;
import com.nunes.financeFlow.models.dtos.AuthenticationDTO;
import com.nunes.financeFlow.models.dtos.RegisterDTO;
import com.nunes.financeFlow.models.dtos.UsuarioDto;
import com.nunes.financeFlow.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.nunes.financeFlow.services.UsuarioService;
import com.nunes.financeFlow.shared.ApiResponse;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO loginData) {
        var usernamePasswordToken = new UsernamePasswordAuthenticationToken(loginData.getLogin(), loginData.getPassword());
        var auth = authenticationManager.authenticate(usernamePasswordToken);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("auth/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerData) {
        if (userRepository.findByLogin(registerData.getLogin()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(registerData.getPassword());
        User newUser = new User(registerData.getLogin(), encodedPassword, registerData.getRole());
        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
