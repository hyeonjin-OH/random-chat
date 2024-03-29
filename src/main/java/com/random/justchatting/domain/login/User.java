package com.random.justchatting.domain.login;

import com.random.justchatting.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String apiKey;
    private String uuId;
    private String nickName;
    private List<String> rooms = new ArrayList<>();

    @Builder
    public User(Long id, String apiKey, String uuId, String nickName, List<String> rooms) {
        this.id = id;
        this.apiKey = apiKey;
        this.uuId = uuId;
        this.nickName = nickName;
        this.rooms = rooms;
    }

    public static User createUId(User user){
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hhmmss"));
        return User.builder()
                .apiKey(user.getApiKey())
                .uuId(now.substring(0,4) + user.getApiKey().substring(user.getApiKey().length()-6, user.getApiKey().length())
                        + now.substring(4,6))
                .nickName("")
                .rooms(user.getRooms())
                .build();
    }

    public static UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(user.id)
                .apiKey(user.apiKey)
                .uuId(user.uuId)
                .nickName(user.nickName)
                .rooms(user.rooms)
                .build();
    }

    public static User from(UserEntity user){
        return User.builder()
                .id(user.getId())
                .apiKey(user.getApiKey())
                .uuId(user.getUuId())
                .nickName(user.getNickName())
                .rooms(user.getRooms())
                .build();
    }

    public void encodeApiKey(String encodedApiKey){
        this.apiKey = encodedApiKey;
    }

    public void enterRoom(String roomKey){
        if(this.rooms == null){
            this.rooms = new ArrayList<>();
        }
        this.rooms.add(roomKey);
    }

    public void exitRoom(String roomKey){
        this.rooms.remove(roomKey);
    }
}
