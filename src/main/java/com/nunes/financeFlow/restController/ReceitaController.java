package com.nunes.financeFlow.restController;

import com.nunes.financeFlow.models.Receita;
import com.nunes.financeFlow.models.dtos.ReceitaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nunes.financeFlow.services.ReceitaService;
import com.nunes.financeFlow.shared.ApiResponse;

import java.util.List;


@RestController
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping("/receitas")
    public ApiResponse<ReceitaDto> save(@RequestBody ReceitaDto receitaDto){
        return this.receitaService.save(receitaDto);
    }

    @GetMapping("/listaReceitas")
    public ApiResponse<List<ReceitaDto>> findAll(){
        return  this.receitaService.findAll();
    }

    @GetMapping("/getReceita/{id}")
    public ApiResponse<ReceitaDto> findById(@PathVariable Long id){
        return this.receitaService.findById(id);
    }

    @DeleteMapping("receitas/deletar/{id}")
    public ApiResponse<ReceitaDto> deleteById(@PathVariable Long id) {
        return receitaService.deleteById(id);
    }

    @PutMapping("/receitas/atualizar/{id}")
    public ResponseEntity<ApiResponse<ReceitaDto>> updateById(@PathVariable Long id, @RequestBody ReceitaDto receitaDto){
        System.out.println("Recebendo chamada PUT /receitas/" + id);
        System.out.println("ID no path: " + id);
        System.out.println("ID no body: " + receitaDto.getId());
        return ResponseEntity.ok(receitaService.updateById(id, receitaDto));
    }
}
