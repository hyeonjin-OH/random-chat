package com.random.justchatting.controller.match;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.match.MatchReq;
import com.random.justchatting.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MatchController {

    private final RedisService redisService;
    private final SimpMessageSendingOperations template;

    @PostMapping("/match")
    public ResponseEntity<?> matchingChat(@AuthenticationPrincipal UserDetails user, @RequestBody MatchReq req){
        long timeMillis = System.currentTimeMillis();

        String uuId = user.getUsername();
        ChatRoom room = new ChatRoom();
        String matchKey = redisService.preferMatching(req);
        // 매칭
        if(matchKey.equals("")){
            // 매칭에 만족하는 값이 없다면 대기리스트에 추가
            room = redisService.addUserOptions(timeMillis, req);
            template.convertAndSend("/sub/matchStatus/"+room.getRoomKey(), "검색중");

        }
        else{
            return ResponseEntity.ok().body(matchKey);
        }

        return ResponseEntity.accepted().body(room);
    }

    @PostMapping("/match/cancel")
    public ResponseEntity<?> cancelMatching(@AuthenticationPrincipal UserDetails user, @RequestBody MatchReq req){
        long timeMillis = System.currentTimeMillis();

        String uuId = user.getUsername();
        ChatRoom room = new ChatRoom();

        redisService.cancelMatch(req);

        return ResponseEntity.ok().body("Cancel");
    }
}
