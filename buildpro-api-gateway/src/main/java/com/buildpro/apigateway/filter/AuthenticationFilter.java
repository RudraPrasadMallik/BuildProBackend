package com.buildpro.apigateway.filter;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import com.buildpro.apigateway.util.JwtUtil;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;
    
    private static final List<String> openEndpoints = List.of(
        "/auth/**", 
        "/v3/api-docs/**", 
        "/swagger-ui/**", 
        "/swagger-resources/**",
        "/webjars/**",
        "/swagger-ui.html",
        "/favicon.ico"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("🔍 Path received: " + path);
        // Single check for open endpoints (remove the duplicate check)
        if (openEndpoints.stream().anyMatch(path::contains)) {
        	 System.out.println("✅ Bypassing auth for: " + path);
            return chain.filter(exchange);
        }

        // JWT Validation for protected endpoints
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}