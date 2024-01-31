package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public ChatRoom findByRoomKey(String roomKey) {
        return ChatRoom.from(chatRoomJpaRepository.findByRoomKey(roomKey));
    }
}
