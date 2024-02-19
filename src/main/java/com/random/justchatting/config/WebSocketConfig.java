package com.random.justchatting.config;


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
        // 메세지 구독 경로
        config.enableSimpleBroker("/sub");
        // 메시지 보낼 때 관련 경로 설정
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Client에서 websocket 연결할 때 사용할 API 경로를 설정 - 채팅용
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        //Client에서 websocket 연결할 때 사용할 API 경로를 설정 - 매칭용
        registry.addEndpoint("/match")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
