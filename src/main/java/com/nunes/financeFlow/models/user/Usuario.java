package com.nunes.financeFlow.models.user;

import com.nunes.financeFlow.models.Conta;
import com.nunes.financeFlow.models.Despesa;
import com.nunes.financeFlow.models.Receita;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;  // Mapeado como "login" na lógica do User

    @Column(nullable = false, length = 60)
    private String senha;  // Mapeado como "password" na lógica do User

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Despesa> despesas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receita> receitas;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Conta conta; // Um usuário tem uma única conta

    // Implementação de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Lógica de autorização, aqui você pode adicionar as roles conforme necessário
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email; // O Spring usa este método para o login, mapeando o email como login
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha (que será mapeada para password)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Definindo como true para a conta nunca expirar
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Definindo como true para a conta nunca estar bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Definindo como true para as credenciais nunca expirarem
    }

    @Override
    public boolean isEnabled() {
        return true; // Definindo como true para a conta estar habilitada
    }
}
