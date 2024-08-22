package com.nunes.financeFlow.models.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.nunes.financeFlow.enumerator.TipoDespesa;
import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Despesa;
import com.nunes.financeFlow.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaDto {

    private Long id;
    private String nome;
    private double valor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    private String descricao;
    private TipoDespesa tipoDespesa;
    private Long idUsuario;
    private Long idConta;

    public DespesaDto(Despesa despesa){
        this.id = despesa.getId();
        this.data = despesa.getData();
        this.valor = despesa.getValor();
        this.descricao = despesa.getDescricao();
        this.tipoDespesa = despesa.getTipoDespesa();
        this.nome = despesa.getNome();
        this.idUsuario = despesa.getUsuario() != null ? despesa.getUsuario().getId() : null;
        this.idConta = despesa.getConta().getId();
    }

    public static Despesa convert(DespesaDto despesaDto, Usuario usuario, Conta conta){
        Despesa despesa = new Despesa();

        despesa.setId(despesaDto.getId());
        System.out.println("Convertendo DespesaDto para Despesa. ID: " + despesaDto.getId());
        despesa.setData(despesaDto.getData());
        despesa.setValor(despesaDto.getValor());
        despesa.setDescricao(despesaDto.getDescricao());
        despesa.setTipoDespesa(despesaDto.getTipoDespesa());
        despesa.setNome(despesaDto.getNome());
        // Associa o usuário ao objeto Despesa
        despesa.setUsuario(usuario);
        despesa.setConta(conta);

        return  despesa;
    }

}
