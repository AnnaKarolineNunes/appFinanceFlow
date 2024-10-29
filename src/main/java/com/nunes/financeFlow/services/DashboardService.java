package com.nunes.financeFlow.services;

import com.nunes.financeFlow.repositories.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nunes.financeFlow.repositories.ReceitaRepository;
import com.nunes.financeFlow.shared.ApiResponse;

@Service
public class DashboardService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    public ApiResponse<Double> getTotalReceitas(Long userId) {
        Double total = receitaRepository.sumByUsuarioId(userId);
        if (total == null) {
            total = 0.0; // Tratando valor nulo como zero
        }
        return new ApiResponse<>(200, "Total de receitas", total);
    }

    public ApiResponse<Double> getTotalDespesas(Long userId) {
        Double total = despesaRepository.sumByUsuarioId(userId);
        if (total == null) {
            total = 0.0; // Tratando valor nulo como zero
        }
        return new ApiResponse<>(200, "Total de despesas", total);
    }

    public ApiResponse<Double> getSaldo(Long userId) {
        Double totalReceitas = receitaRepository.sumByUsuarioId(userId);
        Double totalDespesas = despesaRepository.sumByUsuarioId(userId);

        // Tratando valores nulos como zero
        if (totalReceitas == null) {
            totalReceitas = 0.0;
        }
        if (totalDespesas == null) {
            totalDespesas = 0.0;
        }

        Double saldo = totalReceitas - totalDespesas;
        return new ApiResponse<>(200, "Saldo atual", saldo);
    }
}
