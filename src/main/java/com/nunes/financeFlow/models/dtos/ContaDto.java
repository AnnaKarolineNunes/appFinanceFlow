package com.nunes.financeFlow.models.dtos;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.user.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaDto {

    private Long id;
    private Long idUsuario; // Representa o ID do usuário associado


    // Construtor a partir da entidade Conta
    public ContaDto(Conta conta) {
        this.id = conta.getId();
        if (conta.getUsuario() != null) {
            this.idUsuario = conta.getUsuario().getId();
        }
    }

    // Método para converter ContaDto em Conta
    public static Conta convert(ContaDto contaDto, Usuario usuario) {
        Conta conta = new Conta();

        conta.setId(contaDto.getId());
        conta.setId(contaDto.getId());
        conta.setUsuario(usuario); // Agora o usuário já é buscado previamente
        return conta;
    }

}
