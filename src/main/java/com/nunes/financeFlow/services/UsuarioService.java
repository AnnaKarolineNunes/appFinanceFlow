package com.nunes.financeFlow.services;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.user.Usuario;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import com.nunes.financeFlow.shared.ApiResponse;
import com.nunes.financeFlow.models.dtos.AuthDto;
import com.nunes.financeFlow.models.dtos.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    PasswordEncoder passwordEncoder; // Injetando PasswordEncoder


    public ApiResponse<List<UsuarioDto>> findAll() {
        try {
            List<Usuario> usuarios = this.usuarioRepository.findAll();
            return new ApiResponse<>(200, "Listagem de usuários realizada com sucesso!",
                    usuarios.stream().map(UsuarioDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<UsuarioDto> findById(Long id) {
        try {
            Optional<Usuario> resultado = this.usuarioRepository.findById(id);

            return resultado.map(usuario -> new ApiResponse<>(200, "Detalhamento de usuário realizado com sucesso!",
                    new UsuarioDto(usuario))).orElseGet(() -> new ApiResponse<>(204, "Usuário não encontrado!", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<UsuarioDto> updateById(Long id, UsuarioDto dto) {
        try {
            // Buscar o usuário existente no banco de dados
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            if (usuarioOpt.isEmpty()) {
                return new ApiResponse<>(404, "Não é possível editar, pois usuário não foi encontrado por ID!", null);
            }

            Usuario usuarioExistente = usuarioOpt.get();

            // Verificar se o email já está em uso por outro usuário
            if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                return new ApiResponse<>(409, "Não é possível editar, pois já existe outro usuário com esse email!", null);
            }

            // Atualizar apenas os campos necessários
            usuarioExistente.setNome(dto.getNome());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setSenha(dto.getSenha());
            // Outros campos podem ser atualizados aqui conforme necessário

            // Salvar as mudanças no banco de dados
            usuarioExistente = usuarioRepository.save(usuarioExistente);

            return new ApiResponse<>(200, "Usuário editado com sucesso!", new UsuarioDto(usuarioExistente));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }
}