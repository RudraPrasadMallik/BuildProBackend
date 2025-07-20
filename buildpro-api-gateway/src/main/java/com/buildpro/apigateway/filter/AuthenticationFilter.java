package com.buildpro.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.util.pattern.PathPatternParser;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements WebFilter {

    private static final String[] PUBLIC_PATHS = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        for (String publicPath : PUBLIC_PATHS) {
            if (pathPatternParser.parse(publicPath).matches(exchange.getRequest().getPath())) {
                return chain.filter(exchange); // Allow public paths
            }
        }

        // If Authorization header is missing, reject
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header"));
        }

        // TODO: Optionally validate token here

        return chain.filter(exchange); // Continue if token is valid
    }
}
