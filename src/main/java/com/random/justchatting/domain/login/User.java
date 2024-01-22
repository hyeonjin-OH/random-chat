package com.random.justchatting.domain.login;

import com.random.justchatting.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {

    public Long id;
    public String apiKey;
    public String nickName;
    public String mainCharacter;


    public static UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(user.id)
                .apiKey(user.apiKey)
                .nickName(user.nickName)
                .mainCharacter(user.mainCharacter)
                .build();
    }

    public static User from(UserEntity user){
        return User.builder()
                .id(user.getId())
                .apiKey(user.getApiKey())
                .nickName(user.getNickName())
                .mainCharacter(user.getMainCharacter())
                .build();
    }
}
