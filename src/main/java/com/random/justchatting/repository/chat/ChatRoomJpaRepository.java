package com.random.justchatting.repository.chat;

import com.random.justchatting.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findByRoomKey(String roomKey);

    @Query(value="select * from room c where c.room_id = :roomid and (c.receiver_id = :uuid or c.sender_id = :uuid)",
    nativeQuery = true)
    ChatRoomEntity findByRoomIdAndUuId(@Param("roomid")Long roomId, @Param("uuid")String uuId);

    ChatRoomEntity findByRoomId(Long roomId);
}
