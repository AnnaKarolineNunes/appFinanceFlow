package com.nunes.financeFlow.models;

import com.nunes.financeFlow.enumerator.TipoConta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_conta", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    public Conta(Long id) {
        this.id = id;
    }
}
