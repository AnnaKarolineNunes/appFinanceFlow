package com.nunes.financeFlow.models;

import com.nunes.financeFlow.enumerator.TipoReceita;
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
@Table(name = "receita")
public class Receita {

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

    @Column(name = "tipo_receita", nullable = false)
    @Enumerated(EnumType.STRING) // Mapeia o enum como uma string
    private TipoReceita tipoReceita;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)  // Define a chave estrangeira para a tabela de usuários
    private User user;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

}
