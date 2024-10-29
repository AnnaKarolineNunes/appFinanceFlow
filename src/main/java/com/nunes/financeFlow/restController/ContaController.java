package com.nunes.financeFlow.restController;

import com.nunes.financeFlow.models.dtos.ContaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nunes.financeFlow.services.ContaService;
import com.nunes.financeFlow.shared.ApiResponse;

import java.util.List;

@RequestMapping("contas")
@RestController
public class ContaController {

    @Autowired
    private ContaService contaService;

    // POST
    @PostMapping("/salvarConta")
    public ApiResponse<ContaDto> save(@RequestBody ContaDto dto){
        return this.contaService.save(dto);
    }

    // GET
    @GetMapping("/ListaContas")
    public ApiResponse<List<ContaDto>> findAll(){
        return this.contaService.findAll();
    }

    @GetMapping("/GetContaId/{id}")
    public ApiResponse<ContaDto> findById(@PathVariable Long id){
        return this.contaService.findById(id);
    }

    // PUT
    @PutMapping("/UpContaId/{id}")
    public ApiResponse<ContaDto> updateById(@PathVariable Long id, @RequestBody ContaDto dto){
        return this.contaService.updateById(id, dto);
    }

    // DELETE
    @DeleteMapping("/DelContaId/{id}")
    public ApiResponse<ContaDto> deleteById(@PathVariable Long id){
        return this.contaService.deleteById(id);
    }
}
