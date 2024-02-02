package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;

public interface ChatRepository {

    ChatMessages saveMessages(ChatMessages messages, ChatMessages.MessageType type);

}
