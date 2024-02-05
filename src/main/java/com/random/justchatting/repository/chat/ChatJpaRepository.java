package com.random.justchatting.repository.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.entity.ChatMessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatJpaRepository extends JpaRepository<ChatMessagesEntity, Long> {

    List<ChatMessagesEntity> findAllByRoomKey(String roomKey);

}
