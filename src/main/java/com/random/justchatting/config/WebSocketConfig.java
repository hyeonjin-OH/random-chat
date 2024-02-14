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
        /*
        메시지 받을 때 관련 경로 설정 - 기본 제공
        "/queue", "/topic" 이 두 경로가 prefix(api 경로 맨 앞)에 붙은 경우, messageBroker가 잡아서 해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해줌
        주로 "/queue"는 1대1 메시징, "/topic"은 1대다 메시징일 때 사용
         */
        // 메세지 구독 경로
        config.enableSimpleBroker("/sub");
        // 메시지 보낼 때 관련 경로 설정
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Client에서 websocket 연결할 때 사용할 API 경로를 설정해주는 메서드 - 채팅용
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/match")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
