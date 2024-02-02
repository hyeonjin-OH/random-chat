package com.random.justchatting.domain.chat;

import com.random.justchatting.entity.ChatMessagesEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessages {

    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    private MessageType type;
    private String roomKey;
    private String sender;
    private String message;
    private LocalDateTime sendTime;

    public static ChatMessagesEntity toEntity(ChatMessages messages){
        return ChatMessagesEntity.builder()
                .roomKey(messages.getRoomKey())
                .senderId(messages.sender)
                .messages(messages.message)
                .sendTime(messages.sendTime)
                .build();
    }

    public static ChatMessages from(ChatMessagesEntity messages, MessageType type){
        return ChatMessages.builder()
                .type(type)
                .roomKey(messages.getRoomKey())
                .sender(messages.getSenderId())
                .message(messages.getMessages())
                .sendTime(messages.getSendTime())
                .build();
    }
}
