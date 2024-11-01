package com.nunes.financeFlow.models;

import com.nunes.financeFlow.models.user.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Construtor para facilitar a criação de um novo token
    public VerificationToken(String token, Usuario usuario, LocalDateTime expiryDate) {
        this.token = token;
        this.usuario = usuario;
        this.expiryDate = expiryDate;
    }

    // Método para verificar se o token já expirou
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
