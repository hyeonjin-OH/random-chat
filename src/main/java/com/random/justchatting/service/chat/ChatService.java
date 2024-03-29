package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.UserPrefer;

import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom(String uId);

    List<ChatMessages> findAllMessages(String roomKey);

    String findRoomKeyByRoomIdAndUuId(Long roomId, String uuId);


    ChatRoom openChatRoom(String senderId);

    ChatRoom enterChatRoom(String receiverId, String roomKey);

    ChatRoom exitChatRoom(String roomKey, String uuId);

    ChatRoom findRoomInfo(String roomKey);

    void deleteChatRoom(String roomKey);

    String findRoomKeyByRoomId(Long roomId);
}
