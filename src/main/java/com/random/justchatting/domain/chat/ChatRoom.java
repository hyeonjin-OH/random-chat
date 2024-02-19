package com.random.justchatting.domain.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.random.justchatting.entity.ChatRoomEntity;
import lombok.*;

import java.time.LocalDateTime;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    private Long roomId;
    private String roomKey;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;
    private String senderId;
    private String receiverId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastTime;
    private String lastMessage;

    public static ChatRoom create(){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomKey = UUID.randomUUID().toString();
        chatRoom.createdTime = LocalDateTime.now();
        chatRoom.lastTime = LocalDateTime.now();
        chatRoom.lastMessage = "";
        return chatRoom;
    }

    public void setSender(String sender) {
        this.senderId = sender;
    }

    public void setReceiver(String receiver) {
        this.receiverId = receiver;
    }

    public void updateChat(LocalDateTime time, String message){
        this.lastTime = time;
        this.lastMessage = message;
    }

    public static ChatRoomEntity toEntity(ChatRoom room){
        return ChatRoomEntity.builder()
                .roomId(room.roomId)
                .roomKey(room.roomKey)
                .createdTime(room.createdTime)
                .senderId(room.senderId)
                .receiverId(room.receiverId)
                .lastMessage(room.lastMessage)
                .lastTime(room.lastTime)
                .build();
    }

    public static ChatRoom from(ChatRoomEntity room){
        return ChatRoom.builder()
                .roomId(room.getRoomId())
                .roomKey(room.getRoomKey())
                .createdTime(room.getCreatedTime())
                .senderId(room.getSenderId())
                .receiverId(room.getReceiverId())
                .lastMessage(room.getLastMessage())
                .lastTime(room.getLastTime())
                .build();
    }

}
