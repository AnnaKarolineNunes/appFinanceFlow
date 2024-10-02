package com.nunes.financeFlow.infraSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Autenticação
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Usuários (somente admins podem listar todos os usuários)
                        .requestMatchers(HttpMethod.GET, "/usuarios").permitAll()   //.hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/atualizarUsuario/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/deletarUsuario/**").authenticated()

                        // Receitas
                        .requestMatchers(HttpMethod.POST, "/receitas").authenticated() // Criar receitas
                        .requestMatchers(HttpMethod.GET, "/listaReceitas").authenticated()  // Listar receitas
                        .requestMatchers(HttpMethod.GET, "/receita/**").authenticated() // Receita por ID
                        .requestMatchers(HttpMethod.PUT, "/despesas/atualizar/**").authenticated() // Atualizar receitas
                        .requestMatchers(HttpMethod.DELETE, "/despesas/deletar/**").authenticated() // Deletar receitas

                        // Despesas
                        .requestMatchers(HttpMethod.POST, "/despesas").authenticated() // Criar despesas
                        .requestMatchers(HttpMethod.GET, "/listaDespesas").authenticated() // Listar despesas
                        .requestMatchers(HttpMethod.GET, "/despesa/**").authenticated() // Despesa por ID
                        .requestMatchers(HttpMethod.PUT, "/receitas/atualizar/**").authenticated() // Atualizar despesas
                        .requestMatchers(HttpMethod.DELETE, "/receitas/deletar/**").authenticated() // Deletar despesas

                        // Dashboard (consultas financeiras)
                        .requestMatchers(HttpMethod.GET, "/receita/**").authenticated() // Total de receitas
                        .requestMatchers(HttpMethod.GET, "/despesas/**").authenticated() // Total de despesas
                        .requestMatchers(HttpMethod.GET, "/saldo/**").authenticated() // Consultar saldo

                        // Contas
                        .requestMatchers(HttpMethod.POST, "/contas").authenticated() // Criar conta
                        .requestMatchers(HttpMethod.GET, "/contas").authenticated()  // Listar contas
                        .requestMatchers(HttpMethod.PUT, "/contas/**").authenticated() // Atualizar conta
                        .requestMatchers(HttpMethod.DELETE, "/contas/**").authenticated() // Deletar conta

                        .anyRequest().authenticated() // Qualquer outra rota requer autenticação
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
