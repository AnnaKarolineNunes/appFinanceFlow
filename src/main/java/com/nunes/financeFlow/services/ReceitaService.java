package com.nunes.financeFlow.services;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.dtos.ReceitaDto;
import com.nunes.financeFlow.models.Receita;
import com.nunes.financeFlow.models.user.Usuario;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.ReceitaRepository;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import com.nunes.financeFlow.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    // Método para salvar uma nova receita
    public ApiResponse<ReceitaDto> save(ReceitaDto dto) {
        try {
            // Buscar o usuário pelo ID
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdUsuario());
            if (usuarioOpt.isEmpty()) {
                return new ApiResponse<>(404, "Usuário não encontrado!", null);
            }

            // Buscar a conta pelo ID
            Optional<Conta> contaOpt = contaRepository.findById(dto.getIdConta());
            if (contaOpt.isEmpty()) {
                return new ApiResponse<>(404, "Conta não encontrada!", null);
            }

            // Converter DTO para entidade Receita
            Receita receita = ReceitaDto.convert(dto, usuarioOpt.get(), contaOpt.get());

            // Salvar a receita
            receita = receitaRepository.save(receita);

            return new ApiResponse<>(201, "Receita criada com sucesso!", new ReceitaDto(receita));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao criar receita: " + e.getMessage(), null);
        }
    }

    // Método para atualizar uma receita existente
    public ApiResponse<ReceitaDto> updateById(Long id, ReceitaDto receitaDto) {
        if (id == null) {
            System.out.println("Erro: ID é nulo ao tentar atualizar receita.");
            return new ApiResponse<>(500, "Erro ao atualizar receita: ID é nulo.", null);
        }

        System.out.println("Chamando updateById com ID: " + id);

        try {
            Optional<Receita> receitaOpt = this.receitaRepository.findById(id);
            if (receitaOpt.isPresent()) {
                Receita receita = receitaOpt.get();

                // Verificação explícita do ID antes de continuar
                if (receita.getId() == null) {
                    System.out.println("Erro: ID da receita é nulo.");
                    return new ApiResponse<>(500, "Erro ao atualizar receita: ID da receita é nulo.", null);
                }

                Optional<Usuario> usuarioOpt = usuarioRepository.findById(receitaDto.getIdUsuario());
                if (usuarioOpt.isPresent()) {
                    receita.setValor(receitaDto.getValor());
                    receita.setDescricao(receitaDto.getDescricao());
                    receita.setNome(receitaDto.getNome());
                    receita.setData(receitaDto.getData());
                    receita.setTipoReceita(receitaDto.getTipoReceita());
                    receita.setUsuario(usuarioOpt.get());

                    Receita receitaAtualizada = this.receitaRepository.save(receita);

                    return new ApiResponse<>(200, "Receita atualizada com sucesso!", new ReceitaDto(receitaAtualizada));
                } else {
                    return new ApiResponse<>(404, "Usuário não encontrado para o ID fornecido", null);
                }
            } else {
                return new ApiResponse<>(404, "Receita não encontrada para o ID fornecido", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, "Erro ao atualizar receita: " + e.getMessage(), null);
        }
    }

    // Método para retornar todas as receitas
    public ApiResponse<List<ReceitaDto>> findAll() {
        try {
            List<Receita> receitas = this.receitaRepository.findAll();
            return new ApiResponse<>(200, "Listagem de receitas realizada com sucesso!",
                    receitas.stream().map(ReceitaDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    // Método para retornar uma receita pelo seu ID
    public ApiResponse<ReceitaDto> findById(Long id) {
        try {
            Optional<Receita> resultado = this.receitaRepository.findById(id);
            return resultado.map(receita -> new ApiResponse<>(200, "Detalhamento de receita realizado com sucesso!",
                    new ReceitaDto(receita))).orElseGet(() -> new ApiResponse<>(204, "Receita não encontrada!", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    // Método para excluir uma receita pelo seu ID
    public ApiResponse<ReceitaDto> deleteById(Long id) {
        try {
            Optional<Receita> receitaOpt = receitaRepository.findById(id);
            if (receitaOpt.isPresent()) {
                Receita receita = receitaOpt.get();
                receitaRepository.deleteById(id);
                return new ApiResponse<>(200, "Receita excluída com sucesso!", new ReceitaDto(receita));
            } else {
                return new ApiResponse<>(404, "Receita não encontrada para o ID fornecido", null);
            }
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao excluir receita: " + e.getMessage(), null);
        }
    }
}
