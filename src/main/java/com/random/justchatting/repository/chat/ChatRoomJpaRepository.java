package com.random.justchatting.repository.chat;

import com.random.justchatting.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findByRoomKey(String roomKey);


}
