package com.nunes.financeFlow.services;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Usuario;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.UsuarioRepository;
import com.nunes.financeFlow.shared.ApiResponse;
import com.nunes.financeFlow.models.dtos.AuthDto;
import com.nunes.financeFlow.models.dtos.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ApiResponse<UsuarioDto> save(UsuarioDto dto) {
        try {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                return new ApiResponse<>(409, "Já existe outro usuário com esse email!", null);
            }

            if (!Objects.equals(dto.getSenha(), dto.getConfirmaSenha())) {
                return new ApiResponse<>(400, "As senhas digitadas não coincidem. Por favor, verifique e tente novamente", null);
            }

            // Armazenando a senha em texto simples
            Usuario usuario = UsuarioDto.convert(dto);
            usuario.setSenha(dto.getSenha()); // Armazenando a senha em texto simples
            usuario = this.usuarioRepository.save(usuario);

            // Criação automática da conta vinculada ao usuário
            Conta conta = new Conta();
            conta.setUsuario(usuario);
            contaRepository.save(conta);

            return new ApiResponse<>(201, "Usuário cadastrado com sucesso!", new UsuarioDto(usuario));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<UsuarioDto> login(AuthDto dto) {
        try {
            Optional<Usuario> existeUsuario = this.usuarioRepository.findByEmail(dto.getEmail());

            if (existeUsuario.isEmpty()) {
                return new ApiResponse<>(400, "Usuário ou senha inválida!", null);
            }

            // Comparando a senha em texto simples
            if (!existeUsuario.get().getSenha().equals(dto.getSenha())) {
                return new ApiResponse<>(400, "Usuário ou senha inválida!", null);
            }

            return new ApiResponse<>(200, "Login realizado com sucesso!", new UsuarioDto(existeUsuario.get()));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

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
            if (usuarioRepository.existsByEmailAndNotId(dto.getEmail(), id)) {
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


    public ApiResponse<UsuarioDto> deleteById(Long id) {
        try {
            ApiResponse<UsuarioDto> existeUsuario = this.findById(id);

            if (existeUsuario.getStatus() != 200) {
                return new ApiResponse<>(404, "Não foi possível excluir, pois usuário não foi encontrado por ID!", null);
            }

            this.usuarioRepository.deleteById(id);

            return new ApiResponse<>(200, "Usuário excluído com sucesso!", existeUsuario.getData());
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<Void> deleteUser(Long userId) {
        if (usuarioRepository.existsById(userId)) {
            usuarioRepository.deleteById(userId);
            return new ApiResponse<>(200, "Usuário excluído com sucesso!", null);
        } else {
            return new ApiResponse<>(404, "Usuário não encontrado", null);
        }
    }
}