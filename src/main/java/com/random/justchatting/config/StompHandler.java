package com.random.justchatting.config;

import com.random.justchatting.domain.match.MatchReq;
import com.random.justchatting.service.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final HttpSession httpSession;
    private final Map<String, String> sessionInfo = new ConcurrentHashMap<>();
    private final RedisService redisService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(message);

        // CONNECT 프레임의 경우에만 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();

            if (sessionId != null && Objects.equals(accessor.getFirstNativeHeader("endpoint"), "match")) {
                // 연결 요청에서 전달된 파라미터 읽기
                String key = accessor.getFirstNativeHeader("key");
                String value = accessor.getFirstNativeHeader("value");

                sessionInfo.put(sessionId, key + "/" + value);

            }
        }
        return message;
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        if(sessionId!=null){
            // 세션에서 key와 value 가져오기
            String info = sessionInfo.get(sessionId);

            if (info != null) {
                String normalDisconnect = accessor.getDestination();
                assert normalDisconnect != null;
                if(!normalDisconnect.equals("/disconnect")){
                    String[] parts = info.split("/");
                    String key = parts[0];
                    int optionsCount = Integer.valueOf(key.split(":")[0]);
                    String prefer = key.split(":")[1];
                    String value = parts[1];
                    String uuId = value.split(":")[0];
                    String roomKey = value.split(":")[1];

                    MatchReq req = MatchReq.builder()
                            .uuId(uuId)
                            .prefer(prefer)
                            .optionCount(optionsCount)
                            .roomKey(roomKey)
                            .build();
                    redisService.cancelMatch(req);
                }


            }
            // 세션에서 정보 삭제
        }
    }
}
