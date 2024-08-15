package com.nunes.financeFlow.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nunes.financeFlow.services.DashboardService;
import com.nunes.financeFlow.shared.ApiResponse;

@RestController
public class FinanceController {

    @Autowired
    private DashboardService financeService;

    // GET total receitas
    @GetMapping("/receita/{userId}")
    public ApiResponse<Double> getTotalReceitas(@PathVariable Long userId) {
        return financeService.getTotalReceitas(userId);
    }

    // GET total despesas
    @GetMapping("/despesas/{userId}")
    public ApiResponse<Double> getTotalDespesas(@PathVariable Long userId) {
        return financeService.getTotalDespesas(userId);
    }

    // GET saldo
    @GetMapping("/saldo/{userId}")
    public ApiResponse<Double> getSaldo(@PathVariable Long userId) {
        return financeService.getSaldo(userId);
    }
}
