package com.nunes.financeFlow.models;

import com.nunes.financeFlow.enumerator.TipoDespesa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "despesa")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double valor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column
    private String nome;

    @Column
    private String descricao;

    @Column(name = "tipo_despesa", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDespesa tipoDespesa;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)  // Define a chave estrangeira para a tabela de usuários
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;
}
