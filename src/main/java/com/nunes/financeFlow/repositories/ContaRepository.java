package com.nunes.financeFlow.repositories;

import com.nunes.financeFlow.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta,Long> {

}
