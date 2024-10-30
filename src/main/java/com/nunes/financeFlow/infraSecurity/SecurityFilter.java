package com.nunes.financeFlow.infraSecurity;

import com.nunes.financeFlow.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        logger.info("Interceptando a rota: {}", requestURI);

        // Ignora o filtro para as rotas de login e registro
        if (requestURI.equals("/auth/login") || requestURI.equals("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Recupera o token do cabeçalho Authorization
        var token = this.recoverToken(request);
        if (token != null) {
            logger.info("Token encontrado: {}", token);
            var email = tokenService.validateToken(token);

            // Verifica se o email foi recuperado corretamente (token válido)
            if (email != null && !email.isEmpty()) {
                logger.info("Tentando buscar usuário no repositório com email: {}", email);
                UserDetails user = usuarioRepository.findByEmail(email).orElse(null);

                if (user != null) {
                    logger.info("Usuário encontrado: {}. Preparando para autenticar o usuário.", email);

                    // Criação do token com as autoridades
                    var authorities = user.getAuthorities();
                    logger.info("Autoridades atribuídas ao usuário: {}", authorities);

                    var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Usuário autenticado com sucesso. Email: {}, Autoridades: {}", email, authorities);

                    // Log adicional para verificar o contexto de segurança
                    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                        logger.info("Usuário autenticado está presente no contexto de segurança.");
                    } else {
                        logger.warn("Falha ao inserir o usuário no contexto de segurança.");
                    }
                } else {
                    logger.info("Usuário não encontrado: {}", email);
                }
            } else {
                logger.info("Email recuperado do token está nulo ou vazio.");
            }
        } else {
            logger.info("Token não encontrado no cabeçalho Authorization.");
        }


        // Continua o filtro, seja autenticado ou não
        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
