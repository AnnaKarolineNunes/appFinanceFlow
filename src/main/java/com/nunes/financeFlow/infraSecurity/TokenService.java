package com.nunes.financeFlow.infraSecurity;


import com.auth0.jwt.JWT;
import com.nunes.financeFlow.models.User;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("$api.security.token.secret")
    private String secret;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(user.getLogin())
                .withIssuer("financeFlow-api")
                .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC))
                .sign(algorithm);
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("financeFlow-api")
                .build()
                .verify(token)
                .getSubject();
    }
}
