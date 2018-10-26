package com.orsys.tdd.puissance4.serveur.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class ConfigurateurWebSocket implements WebSocketConfigurer {
 
    @Bean
    public WebSocketHandler myMessageHandler() {
        return new MonGestionnaireTexteSocket();
    }
 
    
    public void registerWebSocketHandlers( WebSocketHandlerRegistry registry ) {
        registry.addHandler(myMessageHandler(), "/puissance4");
    }
 
}