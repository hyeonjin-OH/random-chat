package com.random.justchatting.controller.chat;

import com.amazonaws.Response;
import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.service.chat.ChatService;
import com.random.justchatting.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;
    private final ChatService chatService;


    @GetMapping("/waitingroom")
    public ResponseEntity<?> getWaitingRoom(@AuthenticationPrincipal UserDetails user){
        String uuId = user.getUsername();

        UserPrefer prefer = userService.getUserPreferenceByUId(uuId);
        if (prefer != null){
            return ResponseEntity.ok().body(prefer);
        }
        else{
            return ResponseEntity.ok().body(null);
        }
    }

    @PostMapping("/waitingroom")
    public ResponseEntity<?> findRoom(@RequestBody UserPrefer prefer){

        return ResponseEntity.ok().body(null);
    }

    //채팅 홈
    @GetMapping("/chattingroom")
    public ResponseEntity<?> getChattingRoom(@AuthenticationPrincipal UserDetails user) {
        String uuId = user.getUsername();
        List<ChatRoom> rooms = chatService.findAllRoom(uuId);
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/chattingroom/{roomId}")
    public ResponseEntity<?> getChattingRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetails user) {
        try{
            String uuId = user.getUsername();
            String roomKey = chatService.findRoomKeyByRoomIdAndUuId(roomId, uuId);
            List<ChatMessages> messages = chatService.findAllMessages(roomKey);
            return ResponseEntity.ok().body(messages);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("본인이 참가한 roomId가 아닙니다.");
        }

    }


    @GetMapping("/chat/{roomId}")
    public ResponseEntity<?> getChat(@PathVariable String roomId){

        return ResponseEntity.ok().body("Get TEST : " + roomId);
    }

    @PostMapping("/chat/{roomId}")
    public ResponseEntity<?> createChat(@PathVariable String roomId){
        return ResponseEntity.ok().body("Post TEST : " + roomId);

    }

}
