package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.exception.User.UserException;
import com.random.justchatting.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public ChatRoom findByRoomKey(String roomKey) {
        return ChatRoom.from(chatRoomJpaRepository.findByRoomKey(roomKey));
    }

    @Override
    public ChatRoom findRoomKeyByRoomIdAndUuId(Long roomId, String uuId) {
        return ChatRoom.from(chatRoomJpaRepository.findByRoomIdAndUuId(roomId, uuId));
    }

    @Override
    public ChatRoom updateLastChat(String roomKey, LocalDateTime time, String message) {
        ChatRoom room = ChatRoom.from(chatRoomJpaRepository.findByRoomKey(roomKey));
        room.updateChat(time, message);

        return ChatRoom.from(chatRoomJpaRepository.save(ChatRoom.toEntity(room)));
    }

    @Override
    public ChatRoom saveRoom(ChatRoom room) {
        return ChatRoom.from(chatRoomJpaRepository.save(ChatRoom.toEntity(room)));
    }


}
