package com.random.justchatting.controller.match;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.match.MatchReq;
import com.random.justchatting.service.chat.ChatService;
import com.random.justchatting.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MatchController {

    private final RedisService redisService;
    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    /*
    return값
    매칭 시 - ChatRoom
    대기 시 - Map : roomKey, 대기순위time
     */
    @PostMapping("/match")
    public ResponseEntity<?> matchingChat(@AuthenticationPrincipal UserDetails user, @RequestBody MatchReq req){
        long timeMillis = System.currentTimeMillis();
        HashMap<String, String> response = new HashMap<>();

        String uuId = user.getUsername();
        ChatRoom room = new ChatRoom();
        String matchKey = redisService.preferMatching(req);
        // 매칭
        if(matchKey.equals("")){
            // 매칭에 만족하는 값이 없다면 대기리스트에 추가
            room = redisService.addUserOptions(timeMillis, req);
        }
        else{
            room = chatService.findRoomInfo(matchKey);
            template.convertAndSend("/sub/match/" + matchKey, room);
            return ResponseEntity.ok().body(room);
        }

        response.put("roomKey", room.getRoomKey());
        response.put("time", String.valueOf(timeMillis));

        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/match/cancel")
    public ResponseEntity<?> cancelMatching(@AuthenticationPrincipal UserDetails user, @RequestBody MatchReq req){

        String uuId = user.getUsername();

        redisService.cancelMatch(req);

        return ResponseEntity.ok().body("Cancel");
    }

    @PostMapping("/rematch")
    public ResponseEntity<?> rematchingChat(@AuthenticationPrincipal UserDetails user, @RequestBody MatchReq req){

        String uuId = user.getUsername();
        HashMap<String, String> response = new HashMap<>();
        ChatRoom room = redisService.modifyUserOptions(req);
        String matchKey = redisService.preferMatching(req);

        // 매칭 대기
        if(matchKey.equals("")){
            response.put("roomKey", room.getRoomKey());
            response.put("time", String.valueOf(req.getTime()));

            return ResponseEntity.accepted().body(response);
        }
        else{
            room = chatService.findRoomInfo(matchKey);
            template.convertAndSend("/sub/match/" + matchKey, room);
            return ResponseEntity.ok().body(room);
        }
    }
}
