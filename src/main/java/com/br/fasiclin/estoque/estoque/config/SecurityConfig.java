package com.br.fasiclin.estoque.estoque.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança para o sistema de estoque.
 * 
 * Sistema com 3 usuários específicos:
 * - Matheus: Gerencia Ordem de Compra
 * - Yuri: Gerencia Movimentação de Estoque
 * - Erasmo: Gerencia Validação de Almoxarifado
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Ordem de Compra - Matheus
                .requestMatchers("/ordemcompra/**", "/api/ordens-compra/**").hasRole("MATHEUS")
                
                // Movimentação de Estoque - Yuri
                .requestMatchers("/movimentacaoestoque/**", "/api/estoque/**").hasRole("YURI")
                
                // Validação de Almoxarifado - Erasmo
                .requestMatchers("/validacaoalmoxarifado/**", "/api/produtos/reposicao/**", "/api/produtos/estoque-baixo/**").hasRole("ERASMO")
                
                // API Produtos - todos podem acessar
                .requestMatchers("/api/produtos/**").authenticated()
                
                // Assets e recursos estáticos - públicos
                .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**", "/logo/**").permitAll()
                
                // Swagger - público
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Qualquer outra requisição precisa autenticação
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.realmName("Sistema Fasiclin Estoque"))
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }

    /**
     * Define os 3 usuários do sistema em memória.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails matheus = User.builder()
                .username("matheus")
                .password(passwordEncoder().encode("matheus123"))
                .roles("MATHEUS")
                .build();

        UserDetails yuri = User.builder()
                .username("yuri")
                .password(passwordEncoder().encode("yuri123"))
                .roles("YURI")
                .build();

        UserDetails erasmo = User.builder()
                .username("erasmo")
                .password(passwordEncoder().encode("erasmo123"))
                .roles("ERASMO")
                .build();

        return new InMemoryUserDetailsManager(matheus, yuri, erasmo);
    }

    /**
     * Encoder de senhas BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}