package com.nunes.financeFlow.repositories;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta,Long> {
    // Encontra todas as contas associadas a um usuário específico
    List<Conta> findByUsuario(Usuario usuario);

    // Conta quantas contas estão associadas a um usuário específico
    long countByUsuario(Usuario usuario);

    // Verifica se existe uma conta específica associada a um usuário
    Optional<Conta> findByIdAndUsuario(Long id, Usuario usuario);
}
