package com.nunes.financeFlow.models.dtos;

import com.nunes.financeFlow.enumerator.TipoConta;
import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaDto {
    private Long id;
    private Long idUsuario; // Representa o ID do usuário associado
    private TipoConta tipoConta;

    // Construtor padrão
    public ContaDto() {
    }

    // Construtor com ID
    public ContaDto(Long id) {
        this.id = id;
    }

    // Construtor a partir da entidade Conta
    public ContaDto(Conta conta) {
        this.id = conta.getId();
        this.idUsuario = conta.getUsuario().getId(); // Assume que Usuario possui um método getId()
        this.tipoConta = conta.getTipoConta();
    }

    // Método para converter ContaDto em Conta
    public static Conta convert(ContaDto contaDto) {
        Conta conta = new Conta();
        conta.setId(contaDto.getId());
        // Presume que UsuarioRepository está disponível para buscar o usuário por ID
        // Caso contrário, o usuário deve ser atribuído na lógica de serviço
        Usuario usuario = new Usuario();
        usuario.setId(contaDto.getIdUsuario());
        conta.setUsuario(usuario);
        conta.setTipoConta(contaDto.getTipoConta());
        return conta;
    }
}
