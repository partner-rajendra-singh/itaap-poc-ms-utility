package com.philips.itaap.utility.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorsFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponse().getHeaders().set("Access-Control-Max-Age", "3600");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        exchange.getResponse().getHeaders().set("Access-Control-Expose-Headers", "xsrf-token");
        return chain.filter(exchange);
    }
}