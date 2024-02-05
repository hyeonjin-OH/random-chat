package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatRoom;

public interface ChatRoomRepository {

    ChatRoom findByRoomKey(String roomKey);

    ChatRoom findRoomKeyByRoomIdAndUuId(Long roomId, String uuId);

}
