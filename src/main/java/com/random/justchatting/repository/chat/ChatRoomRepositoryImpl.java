package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public ChatRoom findByRoomKey(String roomKey) {
        return ChatRoom.from(chatRoomJpaRepository.findByRoomKey(roomKey));
    }

    @Override
    public ChatRoom findRoomKeyByRoomIdAndUuId(Long roomId, String uuId) {
        return ChatRoom.from(chatRoomJpaRepository.findByRoomIdAndUuId(roomId, uuId));
    }
}
