package com.nunes.financeFlow.services;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Despesa;
import com.nunes.financeFlow.models.user.Usuario;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.DespesaRepository;
import com.nunes.financeFlow.models.dtos.DespesaDto;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nunes.financeFlow.shared.ApiResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DespesaService {

    @Autowired
    DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    // Método para salvar uma nova receita
    public ApiResponse<DespesaDto> save(DespesaDto despesaDto) {
        try {
            // Buscar o usuário pelo ID
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(despesaDto.getIdUsuario());
            if (usuarioOpt.isEmpty()) {
                return new ApiResponse<>(404, "Usuário não encontrado!", null);
            }

            // Buscar a conta pelo ID
            Optional<Conta> contaOpt = contaRepository.findById(despesaDto.getIdConta());
            if (contaOpt.isEmpty()) {
                return new ApiResponse<>(404, "Conta não encontrada!", null);
            }

            // Converter DTO para entidade Despesa
            Despesa despesa = DespesaDto.convert(despesaDto, usuarioOpt.get(), contaOpt.get());

            // Salvar a despesa
            despesa = despesaRepository.save(despesa);

            return new ApiResponse<>(201, "Despesa criada com sucesso!", new DespesaDto(despesa));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao criar despesa: " + e.getMessage(), null);
        }
    }


    // Método para atualizar uma receita existente
    public ApiResponse<DespesaDto> updateById(Long id, DespesaDto despesaDto) {
        if (id == null) {
            System.out.println("Erro: ID é nulo ao tentar atualizar despesa.");
            return new ApiResponse<>(500, "Erro ao atualizar despesa: ID é nulo.", null);
        }

        System.out.println("Chamando updateById com ID: " + id);

        try {
            Optional<Despesa> despesaOpt = this.despesaRepository.findById(id);
            if (despesaOpt.isPresent()) {
                Despesa despesa = despesaOpt.get();

                // Verificação explícita do ID antes de continuar
                if (despesa.getId() == null) {
                    System.out.println("Erro: ID da despesa é nulo.");
                    return new ApiResponse<>(500, "Erro ao atualizar despesa: ID da despesa é nulo.", null);
                }

                Optional<Usuario> usuarioOpt = usuarioRepository.findById(despesaDto.getIdUsuario());
                if (usuarioOpt.isPresent()) {
                    despesa.setValor(despesaDto.getValor());
                    despesa.setDescricao(despesaDto.getDescricao());
                    despesa.setNome(despesaDto.getNome());
                    despesa.setData(despesaDto.getData());
                    despesa.setTipoDespesa(despesaDto.getTipoDespesa());
                    despesa.setUsuario(usuarioOpt.get());

                    Despesa despesaAtualizada = this.despesaRepository.save(despesa);

                    return new ApiResponse<>(200, "Despesa atualizada com sucesso!", new DespesaDto(despesaAtualizada));
                } else {
                    return new ApiResponse<>(404, "Usuário não encontrado para o ID fornecido", null);
                }
            } else {
                return new ApiResponse<>(404, "Despesa não encontrada para o ID fornecido", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, "Erro ao atualizar despesa: " + e.getMessage(), null);
        }
    }


    // Método para retornar todas as despesas
    public ApiResponse<List<DespesaDto>> findAll() {
        try {
            List<Despesa> despesas = this.despesaRepository.findAll();
            return new ApiResponse<>(200, "Listagem de despesas realizada com sucesso!",
                    despesas.stream().map(DespesaDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    // Método para retornar uma receita pelo seu ID
    public ApiResponse<DespesaDto> findById(Long id) {
        try {
            Optional<Despesa> resultado = this.despesaRepository.findById(id);
            return resultado.map(despesa -> new ApiResponse<>(200, "Detalhamento de despesa realizado com sucesso!",
                    new DespesaDto(despesa))).orElseGet(() -> new ApiResponse<>(204, "Despesa não encontrada!", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    // Método para excluir uma receita pelo seu ID
    public ApiResponse<DespesaDto> deleteById(Long id) {
        try {
            Optional<Despesa> despesaOpt = despesaRepository.findById(id);
            if (despesaOpt.isPresent()) {
                Despesa despesa = despesaOpt.get();
                despesaRepository.deleteById(id);
                return new ApiResponse<>(200, "Despesa excluída com sucesso!", new DespesaDto(despesa));
            } else {
                return new ApiResponse<>(404, "Despesa não encontrada para o ID fornecido", null);
            }
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao excluir despesa: " + e.getMessage(), null);
        }
    }
}
