package com.nunes.financeFlow.repositories;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta,Long> {
    // Encontra todas as contas associadas a um usuário específico
    List<Conta> findByUsuario(User user);

    // Conta quantas contas estão associadas a um usuário específico
    long countByUsuario(User user);

    // Verifica se existe uma conta específica associada a um usuário
    Optional<Conta> findByIdAndUsuario(Long id, User user);
}
