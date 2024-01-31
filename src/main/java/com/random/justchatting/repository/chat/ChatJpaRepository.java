package com.random.justchatting.repository.chat;

import com.random.justchatting.entity.ChatMessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<ChatMessagesEntity, Long> {


}
