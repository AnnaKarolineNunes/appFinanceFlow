package com.nunes.financeFlow.restController;

import com.nunes.financeFlow.models.Despesa;
import com.nunes.financeFlow.models.dtos.DespesaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nunes.financeFlow.services.DespesaService;
import com.nunes.financeFlow.shared.ApiResponse;

import java.util.List;

@RestController
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @PostMapping("/despesas")
    public ApiResponse<DespesaDto> save(@RequestBody DespesaDto despesaDto){
        return this.despesaService.save(despesaDto);
    }

    @GetMapping("/listaDespesas")
    public ApiResponse<List<DespesaDto>> findAll() {
        return this.despesaService.findAll();
    }

    @GetMapping("/despesa/{id}")
    public ApiResponse<DespesaDto> findById(@PathVariable Long id) {
        return this.despesaService.findById(id);
    }

    @DeleteMapping("/deletar/{id}")
    public ApiResponse<DespesaDto> deleteById(@PathVariable Long id) {
        return despesaService.deleteById(id);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ApiResponse<DespesaDto>> updateById(@PathVariable Long id, @RequestBody DespesaDto despesaDto){
        System.out.println("Recebendo chamada PUT /despesas/" + id);
        System.out.println("ID no path: " + id);
        System.out.println("ID no body: " + despesaDto.getId());
        return ResponseEntity.ok(despesaService.updateById(id, despesaDto));
    }
}
