package com.nunes.financeFlow.models.dtos;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.User;
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
        if (conta.getUser() != null) {
            this.idUsuario = conta.getUser().getId();
        }
    }

    // Método para converter ContaDto em Conta
    public static Conta convert(ContaDto contaDto, User user) {
        Conta conta = new Conta();

        conta.setId(contaDto.getId());
        conta.setId(contaDto.getId());
        conta.setUser(user); // Agora o usuário já é buscado previamente
        return conta;
    }

}
