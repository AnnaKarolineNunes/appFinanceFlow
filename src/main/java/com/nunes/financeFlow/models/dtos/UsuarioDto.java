package com.nunes.financeFlow.models.dtos;


import com.nunes.financeFlow.models.user.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    private String confirmaSenha;

    public UsuarioDto(Usuario usuario){
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    public boolean isValidPassword() {
        return this.senha != null && this.senha.equals(this.confirmaSenha);
    }


    public static Usuario convert(UsuarioDto usuarioDto, PasswordEncoder passwordEncoder) {
        if (!usuarioDto.isValidPassword()) {
            throw new IllegalArgumentException("Senha e confirmação de senha não correspondem.");
        }
        Usuario usuario = new Usuario();

        usuario.setId(usuarioDto.getId());
        usuario.setNome(usuarioDto.getNome());
        usuario.setEmail(usuarioDto.getEmail());
        // Criptografando a senha antes de definir
        usuario.setSenha(passwordEncoder.encode(usuarioDto.getSenha()));

        return usuario;
    }
}