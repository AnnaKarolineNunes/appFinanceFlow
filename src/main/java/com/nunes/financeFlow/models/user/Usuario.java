package com.nunes.financeFlow.models.user;

import com.nunes.financeFlow.models.Conta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 60)
    private String senha;

    @Column
    private String nome;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Conta conta;

    @Enumerated(EnumType.STRING) // Para armazenar o valor do enum como String no banco
    @Column(nullable = true) // Define a coluna como opcional
    private UserRole role = UserRole.USER; // Define USER como valor padrão

    @Column(nullable = false)
    private boolean emailVerified = false;  // Novo campo indicando se o e-mail foi verificado

    // Método para marcar o e-mail como verificado
    public void markEmailAsVerified() {
        this.emailVerified = true;
    }

    // Implementação de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); // Usando 'name()' para pegar o valor do enum como String
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
