package com.nunes.financeFlow.repositories;

import com.nunes.financeFlow.models.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DespesaRepository extends JpaRepository<Despesa,Long> {
    boolean existsByNome(String nome);
    @Query("SELECT SUM(d.valor) FROM Despesa d WHERE d.usuario.id = :userId")
    Double sumByUsuarioId(Long userId);
}
