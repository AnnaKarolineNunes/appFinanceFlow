package com.nunes.financeFlow.repositories;

import com.nunes.financeFlow.models.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    boolean existsByNome(String nome);
    @Query("SELECT SUM(r.valor) FROM Receita r WHERE r.usuario.id = :userId")
    Double sumByUsuarioId(Long userId);
}
