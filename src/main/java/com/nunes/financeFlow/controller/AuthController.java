package com.nunes.financeFlow.controller;

import com.nunes.financeFlow.models.dtos.AuthDto;
import com.nunes.financeFlow.models.dtos.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.nunes.financeFlow.services.UsuarioService;
import com.nunes.financeFlow.shared.ApiResponse;

@Controller
public class AuthController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/cadastra-usuario")
    public String register(@ModelAttribute UsuarioDto dto, Model model) {
        ApiResponse<UsuarioDto> usuarioCadastrado = this.usuarioService.save(dto);

        if(usuarioCadastrado.getData() == null) {
            model.addAttribute("erro", usuarioCadastrado.getMessage());
            return "cadastro";
        }

        return "redirect:/login";
    }

    @PostMapping("/login-usuario")
    public String login(@ModelAttribute AuthDto dto, Model model) {
        ApiResponse<UsuarioDto> usuarioLogado = this.usuarioService.login(dto);

        if (usuarioLogado.getData() == null) {
            model.addAttribute("erro", usuarioLogado.getMessage());
            return "login";
        }
        return "homepage";
    }
}