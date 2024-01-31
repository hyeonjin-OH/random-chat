package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatRoom;

import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom(String uId);

}
