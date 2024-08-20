package com.nunes.financeFlow.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nunes.financeFlow.enumerator.TipoReceita;
import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Receita;
import com.nunes.financeFlow.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaDto {

    private Long id;
    private String nome;
    private double valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    private String descricao;
    private TipoReceita tipoReceita;
    private Long idUsuario;
    private Long idConta;

    public ReceitaDto(Receita receita) {
        this.id = receita.getId();
        this.valor = receita.getValor();
        this.data = receita.getData();
        this.descricao = receita.getDescricao();
        this.tipoReceita = receita.getTipoReceita();
        this.nome = receita.getNome();
        this.idUsuario = receita.getUser() != null ? receita.getUser().getId() : null;
        this.idConta = receita.getConta().getId();
    }

    public static Receita convert(ReceitaDto receitaDto, User user, Conta conta) {
        Receita receita = new Receita();

        receita.setId(receitaDto.getId());
        System.out.println("Convertendo ReceitaDto para Receita. ID: " + receitaDto.getId());
        receita.setValor(receitaDto.getValor());
        receita.setData(receitaDto.getData());
        receita.setDescricao(receitaDto.getDescricao());
        receita.setNome(receitaDto.getNome());
        receita.setTipoReceita(receitaDto.getTipoReceita());
        // Associa o usuário ao objeto Receita
        receita.setUser(user);
        receita.setConta(conta);

        return receita;
    }
}
