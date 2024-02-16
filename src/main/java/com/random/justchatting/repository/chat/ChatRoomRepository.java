package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatRoom;

import java.time.LocalDateTime;

public interface ChatRoomRepository {

    ChatRoom findByRoomKey(String roomKey);

    ChatRoom findRoomKeyByRoomIdAndUuId(Long roomId, String uuId);

    ChatRoom updateLastChat(String roomKey, LocalDateTime time, String message);

    ChatRoom saveRoom(ChatRoom room);

    void deleteRoom(ChatRoom room);

}

