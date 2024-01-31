package com.random.justchatting.domain.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessages {

    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    private MessageType type;
    private String roomKey;
    private String sender;
    private String message;
    private String time;

}
