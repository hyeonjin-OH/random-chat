package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.entity.ChatMessagesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository{

    private final ChatJpaRepository chatJpaRepository;
    @Override
    public ChatMessages saveMessages(ChatMessages messages, ChatMessages.MessageType type) {
        ChatMessages chat = ChatMessages.from(chatJpaRepository.save(ChatMessages.toEntity(messages)), type);
        return chat;
    }
}
