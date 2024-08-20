package com.nunes.financeFlow.repositories;

import com.nunes.financeFlow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = :email AND u.id <> :id")
    boolean existsByEmailAndNotId(String email, Long id);

    UserDetails findByLogin(String login);
}
