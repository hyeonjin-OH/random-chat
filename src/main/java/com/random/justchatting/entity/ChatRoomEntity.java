package com.random.justchatting.entity;

import com.random.justchatting.config.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="room")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomKey;

    private LocalDateTime createdTime;

    private String senderId;
    private String receiverId;

}
