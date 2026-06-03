package com.smartcart.gateway.filter;

import com.smartcart.gateway.config.RouteValidator;
import com.smartcart.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    // Validate Token
                    jwtUtil.validateToken(authHeader);

                    // Extract claims and mutate request
                    Claims claims = jwtUtil.extractClaims(authHeader);
                    String username = claims.getSubject();
                    String role = claims.get("role", String.class);

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header("loggedInUser", username)
                                    .header("loggedInRole", role)
                                    .build())
                            .build();
                            
                    String path = exchange.getRequest().getURI().getPath();
                    if (path.startsWith("/api/products") && !"ADMIN".equals(role)) {
                        return onError(exchange, "Admin access required");
                    }

                    return chain.filter(mutatedExchange);

                } catch (Exception e) {
                    System.out.println("Invalid token...!" + e.getMessage());
                    return onError(exchange, "Unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}
