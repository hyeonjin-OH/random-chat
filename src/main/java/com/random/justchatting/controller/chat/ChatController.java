package com.random.justchatting.controller.chat;

import com.amazonaws.Response;
import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.chat.ChatRoomReq;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.exception.Chat.ChatException;
import com.random.justchatting.exception.User.UserException;
import com.random.justchatting.service.chat.ChatService;
import com.random.justchatting.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
            //String roomKey = chatService.findRoomKeyByRoomId(roomId);
            ChatRoom room = chatService.findRoomInfo(roomKey);

            return ResponseEntity.ok().body(room);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("존재하는 정보가 아닙니다.");
        }

    }

    @GetMapping("/chattingroom/exit/{roomId}")
    public ResponseEntity<?> getExitChattingRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetails user) {
        try{
            String roomKey = chatService.findRoomKeyByRoomId(roomId);
            ChatRoom room = chatService.findRoomInfo(roomKey);

            return ResponseEntity.ok().body(room);
        }catch(NullPointerException e){
            return ResponseEntity.ok().body(null);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("존재하는 정보가 아닙니다.");
        }

    }

    //채팅방 메세지
    @GetMapping("/chattingroom/messages/{roomId}")
    public ResponseEntity<?> getChattingRoomMessages(@PathVariable Long roomId, @AuthenticationPrincipal UserDetails user) {
        try{
            String uuId = user.getUsername();
            String roomKey = chatService.findRoomKeyByRoomIdAndUuId(roomId, uuId);
            List<ChatMessages> messages = chatService.findAllMessages(roomKey);

            if(messages == null){
                return ResponseEntity.badRequest().body("대화방이 존재하지 않습니다.");
            }
            return ResponseEntity.ok().body(messages);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("본인이 참가한 roomId가 아닙니다.");
        }

    }

    @PostMapping("/chattingroom/exit")
    public ResponseEntity<?> exitChattingRoom(@AuthenticationPrincipal UserDetails user, @RequestBody ChatRoomReq room) {
        try{
            chatService.exitChatRoom(room.getRoomKey(), room.getUuId());
            chatService.deleteChatRoom(room.getRoomKey());

            List<ChatRoom> rooms = chatService.findAllRoom(room.getUuId());
            return ResponseEntity.ok().body(rooms);
        }catch (UserException | ChatException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
