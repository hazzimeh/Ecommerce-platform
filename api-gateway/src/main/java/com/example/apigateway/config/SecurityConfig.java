package com.example.apigateway.config;

import com.example.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/users/register", "/users/login").permitAll()
                .pathMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll() // Allow access to Swagger endpoints
                .pathMatchers(HttpMethod.GET, "/products/**").permitAll() // Allow all users to perform GET requests
                .pathMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN") // Only ADMIN role can add products
                .pathMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN") // Only ADMIN role can update products
                .pathMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN") // Only ADMIN role can delete products
                .anyExchange().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic().disable()
                .formLogin().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
