package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;

import java.util.List;

public interface ChatRepository {

    ChatMessages saveMessages(ChatMessages messages, ChatMessages.MessageType type);

    List<ChatMessages> getAllMessages(String roomKey);

    ChatRoom findChatRoomByRoomIdAndUuId(Long roomId, String uuId);

    void deleteAllMessages(String roomKey);
}
