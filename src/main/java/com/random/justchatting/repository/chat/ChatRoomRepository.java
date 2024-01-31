package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {

    ChatRoom findByRoomKey(String roomKey);

}
