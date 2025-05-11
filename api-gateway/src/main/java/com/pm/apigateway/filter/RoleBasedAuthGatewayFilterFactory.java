package com.pm.apigateway.filter;



import com.pm.apigateway.DTO.RoleResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RoleBasedAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static final Logger log = LoggerFactory.getLogger(RoleBasedAuthGatewayFilterFactory.class);
    private final WebClient webClient;


    public RoleBasedAuthGatewayFilterFactory (WebClient.Builder webClientBuilder, @Value("${auth.service.url}") String authServiceUrl){
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);


            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            log.info("Authorization called!");

            return webClient.get()
                    .uri("/authorize")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(RoleResponseDTO.class)
                    .map(RoleResponseDTO::getRole)
                    .flatMap(role -> {
                        if(!"ADMIN".equals(role)) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    })
                    .onErrorResume(error -> {
                        log.error("Authorization error: {}" , error.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        });
    }

}
