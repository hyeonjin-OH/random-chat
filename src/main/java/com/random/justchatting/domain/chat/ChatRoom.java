package com.random.justchatting.domain.chat;

import com.random.justchatting.entity.ChatRoomEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ChatRoom {

    private Long roomId;
    private String roomKey;
    private LocalDateTime createdTime;
    private String senderId;
    private String receiverId;

    public static ChatRoom create(){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomKey = UUID.randomUUID().toString();
        chatRoom.createdTime = LocalDateTime.now();
        return chatRoom;
    }

    @Builder
    public ChatRoom(Long roomId, String roomKey, LocalDateTime createdTime, String senderId, String receiverId) {
        this.roomId = roomId;
        this.roomKey = roomKey;
        this.createdTime = createdTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public void setSender(String sender) {
        this.senderId = sender;
    }

    public void setReceiver(String receiver) {
        this.receiverId = receiver;
    }

    public static ChatRoomEntity toEntity(ChatRoom room){
        return ChatRoomEntity.builder()
                .roomId(room.roomId)
                .roomKey(room.roomKey)
                .createdTime(room.createdTime)
                .senderId(room.senderId)
                .receiverId(room.receiverId)
                .build();
    }

    public static ChatRoom from(ChatRoomEntity room){
        return ChatRoom.builder()
                .roomId(room.getRoomId())
                .roomKey(room.getRoomKey())
                .createdTime(room.getCreatedTime())
                .senderId(room.getSenderId())
                .receiverId(room.getReceiverId())
                .build();
    }

}
