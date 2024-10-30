package com.nunes.financeFlow.restController;

import com.nunes.financeFlow.models.dtos.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nunes.financeFlow.services.UsuarioService;
import com.nunes.financeFlow.shared.ApiResponse;

import java.util.List;

@RequestMapping("usuarios")
@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/ListaUsuarios")
    public ApiResponse<List<UsuarioDto>> findAll() {
        return this.usuarioService.findAll();
    }

    @GetMapping("/getUsuarios/{id}")
    public ApiResponse<UsuarioDto> findById(@PathVariable Long id) {
        return this.usuarioService.findById(id);
    }

    @PutMapping("/putUsuario/{id}")
    public ApiResponse<UsuarioDto> updateById(@PathVariable Long id, @RequestBody UsuarioDto dto) {
        return this.usuarioService.updateById(id, dto);
    }
}
