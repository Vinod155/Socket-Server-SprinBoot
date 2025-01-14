package com.example.UberSocketServer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/app"); //when receive message from frontend we recieve it on /app/something
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("here");
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*","http://127.0.0.1:3000","http://localhost:3000").withSockJS();
    }
}