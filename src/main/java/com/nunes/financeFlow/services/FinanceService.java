package com.nunes.financeFlow.services;


import com.nunes.financeFlow.repositories.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nunes.financeFlow.repositories.ReceitaRepository;
import com.nunes.financeFlow.shared.ApiResponse;

@Service
public class FinanceService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    public ApiResponse<Double> getTotalReceitas(Long userId) {
        Double total = receitaRepository.sumByUsuarioId(userId);
        return new ApiResponse<>(200, "Total de receitas", total);
    }

    public ApiResponse<Double> getTotalDespesas(Long userId) {
        Double total = despesaRepository.sumByUsuarioId(userId);
        return new ApiResponse<>(200, "Total de despesas", total);
    }

    public ApiResponse<Double> getSaldo(Long userId) {
        Double totalReceitas = receitaRepository.sumByUsuarioId(userId);
        Double totalDespesas = despesaRepository.sumByUsuarioId(userId);
        Double saldo = totalReceitas - totalDespesas;
        return new ApiResponse<>(200, "Saldo atual", saldo);
    }
}

