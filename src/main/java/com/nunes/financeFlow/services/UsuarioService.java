package com.nunes.financeFlow.services;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.User;
import com.nunes.financeFlow.repositories.ContaRepository;
import com.nunes.financeFlow.repositories.UserRepository;
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
    UserRepository userRepository;

    @Autowired
    ContaRepository contaRepository;

    public ApiResponse<UsuarioDto> save(UsuarioDto dto) {
        try {
            if (userRepository.existsByEmail(dto.getLogin())) {
                return new ApiResponse<>(409, "Já existe outro usuário com esse email!", null);
            }

            if (!Objects.equals(dto.getPassword(), dto.getConfirmaSenha())) {
                return new ApiResponse<>(400, "As senhas digitadas não coincidem. Por favor, verifique e tente novamente", null);
            }

            // Armazenando a senha em texto simples
            User user = UsuarioDto.convert(dto);
            user.setPassword(dto.getPassword()); // Armazenando a senha em texto simples
            user = this.userRepository.save(user);

            // Criação automática da conta vinculada ao usuário
            Conta conta = new Conta();
            conta.setUser(user);
            contaRepository.save(conta);

            return new ApiResponse<>(201, "Usuário cadastrado com sucesso!", new UsuarioDto(user));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<UsuarioDto> login(AuthDto dto) {
        try {
            Optional<User> existeUsuario = this.userRepository.findByEmail(dto.getEmail());

            if (existeUsuario.isEmpty()) {
                return new ApiResponse<>(400, "Usuário ou senha inválida!", null);
            }

            // Comparando a senha em texto simples
            if (!existeUsuario.get().getPassword().equals(dto.getSenha())) {
                return new ApiResponse<>(400, "Usuário ou senha inválida!", null);
            }

            return new ApiResponse<>(200, "Login realizado com sucesso!", new UsuarioDto(existeUsuario.get()));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<List<UsuarioDto>> findAll() {
        try {
            List<User> users = this.userRepository.findAll();
            return new ApiResponse<>(200, "Listagem de usuários realizada com sucesso!",
                    users.stream().map(UsuarioDto::new).collect(Collectors.toList()));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<UsuarioDto> findById(Long id) {
        try {
            Optional<User> resultado = this.userRepository.findById(id);

            return resultado.map(usuario -> new ApiResponse<>(200, "Detalhamento de usuário realizado com sucesso!",
                    new UsuarioDto(usuario))).orElseGet(() -> new ApiResponse<>(204, "Usuário não encontrado!", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<UsuarioDto> updateById(Long id, UsuarioDto dto) {
        try {
            // Buscar o usuário existente no banco de dados
            Optional<User> usuarioOpt = userRepository.findById(id);
            if (usuarioOpt.isEmpty()) {
                return new ApiResponse<>(404, "Não é possível editar, pois usuário não foi encontrado por ID!", null);
            }

            User userExistente = usuarioOpt.get();

            // Verificar se o email já está em uso por outro usuário
            if (userRepository.existsByEmailAndNotId(dto.getLogin(), id)) {
                return new ApiResponse<>(409, "Não é possível editar, pois já existe outro usuário com esse email!", null);
            }

            // Atualizar apenas os campos necessários
            userExistente.setNome(dto.getNome());
            userExistente.setLogin(dto.getLogin());
            userExistente.setPassword(dto.getPassword());
            // Outros campos podem ser atualizados aqui conforme necessário

            // Salvar as mudanças no banco de dados
            userExistente = userRepository.save(userExistente);

            return new ApiResponse<>(200, "Usuário editado com sucesso!", new UsuarioDto(userExistente));
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

            this.userRepository.deleteById(id);

            return new ApiResponse<>(200, "Usuário excluído com sucesso!", existeUsuario.getData());
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    public ApiResponse<Void> deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return new ApiResponse<>(200, "Usuário excluído com sucesso!", null);
        } else {
            return new ApiResponse<>(404, "Usuário não encontrado", null);
        }
    }
}
