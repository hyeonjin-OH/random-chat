package com.random.justchatting.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="ChatMessages")
@Getter
@NoArgsConstructor
public class ChatMessagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String roomKey;
    private Long senderId;
    @Column(length=5000)
    private String messages;

}
