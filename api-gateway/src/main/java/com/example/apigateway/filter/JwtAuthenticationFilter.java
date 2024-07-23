package com.example.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("JwtAuthenticationFilter - filtering request...");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getBody();
                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);
                logger.info("JwtAuthenticationFilter - JWT parsed, username: " + username);

                if (username != null) {
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role)) // Ensure roles are correctly prefixed
                            .collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            new User(username, "", authorities), null, authorities);
                    SecurityContext securityContext = new SecurityContextImpl(auth);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                }
            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                logger.warning("JwtAuthenticationFilter - JWT validation failed: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        logger.info("JwtAuthenticationFilter - No JWT found, continuing filter chain...");
        return chain.filter(exchange);
    }
}
