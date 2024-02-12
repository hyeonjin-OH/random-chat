package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;

import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom(String uId);

    List<ChatMessages> findAllMessages(String roomKey);

    String findRoomKeyByRoomIdAndUuId(Long roomId, String uuId);


    ChatRoom openChatRoom(String senderId);

    ChatRoom enterChatRoom(String receiverId, String roomKey);

    ChatRoom exitChatRoom(String roomKey, String uuId);
}
