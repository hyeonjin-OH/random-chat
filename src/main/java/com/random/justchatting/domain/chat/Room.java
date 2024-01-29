package com.random.justchatting.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Room {

    private Long roomId;
    private String roomKey;

    public static Room create(){
        Room room = new Room();
        room.roomKey = UUID.randomUUID().toString();

        return room;
    }

    @Builder
    public Room(Long roomId, String roomKey) {
        this.roomId = roomId;
        this.roomKey = roomKey;
    }
}
