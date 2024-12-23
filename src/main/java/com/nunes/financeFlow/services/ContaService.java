package com.nunes.financeFlow.services;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.user.Usuario;
import com.nunes.financeFlow.models.dtos.ContaDto;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import com.nunes.financeFlow.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para salvar uma nova conta
    public ApiResponse<ContaDto> save(ContaDto dto) {
        try {
            // Verifica se o usuário associado existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdUsuario());
            if (usuarioOpt.isEmpty()) {
                return new ApiResponse<>(404, "Usuário associado não encontrado!", null);
            }

            // Cria e salva a nova conta
            Conta conta = ContaDto.convert(dto, usuarioOpt.get());
            conta.setUsuario(usuarioOpt.get());

            conta = contaRepository.save(conta);

            return new ApiResponse<>(201, "Conta cadastrada com sucesso!", new ContaDto(conta));
        } catch (DataIntegrityViolationException e) {
            return new ApiResponse<>(400, "Violação de integridade dos dados: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao salvar conta: " + e.getMessage(), null);
        }
    }

    // Método para listar todas as contas
    public ApiResponse<List<ContaDto>> findAll() {
        try {
            List<Conta> contas = contaRepository.findAll();
            List<ContaDto> contasDto = contas.stream().map(ContaDto::new).collect(Collectors.toList());
            return new ApiResponse<>(200, "Listagem de contas realizada com sucesso!", contasDto);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao listar contas: " + e.getMessage(), null);
        }
    }

    // Método para encontrar uma conta por ID
    public ApiResponse<ContaDto> findById(Long id) {
        try {
            Optional<Conta> resultado = contaRepository.findById(id);
            return resultado.map(conta -> new ApiResponse<>(200, "Detalhamento de conta realizado com sucesso!", new ContaDto(conta)))
                    .orElseGet(() -> new ApiResponse<>(404, "Conta não encontrada!", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao buscar conta: " + e.getMessage(), null);
        }
    }

    // Método para atualizar uma conta por ID
    public ApiResponse<ContaDto> updateById(Long id, ContaDto dto) {
        try {
            // Verifica se a conta existe
            Optional<Conta> contaOpt = contaRepository.findById(id);
            if (contaOpt.isEmpty()) {
                return new ApiResponse<>(404, "Conta não encontrada por ID!", null);
            }

            // Verifica se o usuário associado existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdUsuario());
            if (usuarioOpt.isEmpty()) {
                return new ApiResponse<>(404, "Usuário associado não encontrado!", null);
            }

            // Atualiza a conta
            Conta conta = contaOpt.get();
            conta.setUsuario(usuarioOpt.get());

            conta = contaRepository.save(conta);

            return new ApiResponse<>(200, "Conta editada com sucesso!", new ContaDto(conta));
        } catch (DataIntegrityViolationException e) {
            return new ApiResponse<>(400, "Violação de integridade dos dados: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao atualizar conta: " + e.getMessage(), null);
        }
    }

    // Método para excluir uma conta por ID
    public ApiResponse<ContaDto> deleteById(Long id) {
        try {
            Optional<Conta> contaOpt = contaRepository.findById(id);

            if (contaOpt.isEmpty()) {
                return new ApiResponse<>(404, "Conta não encontrada por ID!", null);
            }

            Conta conta = contaOpt.get();
            Usuario usuario = conta.getUsuario();

            // Deletar o usuário irá automaticamente deletar a conta e todas as entidades relacionadas, devido ao cascade
            usuarioRepository.delete(usuario);

            return new ApiResponse<>(200, "Conta e usuário excluídos com sucesso!", new ContaDto(conta));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Erro ao excluir conta: " + e.getMessage(), null);
        }
    }


}
