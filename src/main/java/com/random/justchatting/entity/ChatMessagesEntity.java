package com.random.justchatting.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name="ChatMessages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String roomKey;
    private String senderId;
    private String type;
    @Column(length=5000)
    private String messages;
    LocalDateTime sendTime;

}
