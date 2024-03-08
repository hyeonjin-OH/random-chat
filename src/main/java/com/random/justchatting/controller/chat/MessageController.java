package com.random.justchatting.controller.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.repository.chat.ChatRepository;
import com.random.justchatting.repository.chat.ChatRoomRepository;
import com.random.justchatting.repository.user.UserRepository;
import com.random.justchatting.service.chat.ChatService;
import com.random.justchatting.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @MessageMapping("/chat/enter")
    public void enterUser(ChatMessages chat, SimpMessageHeaderAccessor headerAccessor) {

        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userUUID", chat.getSender());
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomKey());


        chat.setMessage("채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomKey(), chat);
        chatRepository.saveMessages(chat, ChatMessages.MessageType.ENTER);
        chatRoomRepository.updateLastChat(chat.getRoomKey(), chat.getSendTime(), chat.getMessage());


    }

    @MessageMapping("/chat/message")
    public void sendMsg(ChatMessages message) {
        template.convertAndSend("/sub/chat/room/"+ message.getRoomKey(), message);
        chatRepository.saveMessages(message, ChatMessages.MessageType.TALK);
        chatRoomRepository.updateLastChat(message.getRoomKey(), message.getSendTime(), message.getMessage());
    }

    @MessageMapping("/chat/exit")
    public void exitUser(ChatMessages chat) {
        if(chatRoomRepository.findByRoomKey(chat.getRoomKey()) == null)
            chatService.deleteChatRoom(chat.getRoomKey());
        chat.setMessage("채팅방에서 퇴장하였습니다.");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomKey() , chat);
        chatRepository.saveMessages(chat, ChatMessages.MessageType.LEAVE);
        chatRoomRepository.updateLastChat(chat.getRoomKey(), chat.getSendTime(), chat.getMessage());
    }


    }
