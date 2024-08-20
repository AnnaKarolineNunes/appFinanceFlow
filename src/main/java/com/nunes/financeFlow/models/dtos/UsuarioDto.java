package com.nunes.financeFlow.models.dtos;


import com.nunes.financeFlow.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    private String login;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    private String confirmaSenha;

    public UsuarioDto(User user){
        this.id = user.getId();
        this.nome = user.getNome();
        this.password = user.getPassword();
        this.login = user.getLogin();
    }

    public boolean isValidPassword() {
        return this.password != null && this.password.equals(this.confirmaSenha);
    }


    public static User convert(UsuarioDto usuarioDto){

        if (!usuarioDto.isValidPassword()) {
            throw new IllegalArgumentException("Senha e confirmação de senha não correspondem.");
        }
        User user = new User();

        user.setId(usuarioDto.getId());
        user.setNome(usuarioDto.getNome());
        user.setPassword(usuarioDto.getPassword());
        user.setLogin(usuarioDto.getLogin());
        return user;
    }
}
