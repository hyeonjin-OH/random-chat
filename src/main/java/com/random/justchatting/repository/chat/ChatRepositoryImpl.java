package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.entity.ChatMessagesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository{

    private final ChatJpaRepository chatJpaRepository;
    @Override
    public ChatMessages saveMessages(ChatMessages messages, ChatMessages.MessageType type) {
        ChatMessages chat = ChatMessages.from(chatJpaRepository.save(ChatMessages.toEntity(messages)));
        return chat;
    }

    @Override
    public List<ChatMessages> getAllMessages(String roomKey) {
        List<ChatMessages> messages = chatJpaRepository.findAllByRoomKey(roomKey)
                .stream().map(list -> ChatMessages.from(list))
                .collect(Collectors.toList());
        return messages;
    }

    @Override
    public ChatRoom findChatRoomByRoomIdAndUuId(Long roomId, String uuId) {
        return null;
    }

    @Override
    public void deleteAllMessages(String roomKey) {
        chatJpaRepository.deleteByRoomKey(roomKey);
    }
}
