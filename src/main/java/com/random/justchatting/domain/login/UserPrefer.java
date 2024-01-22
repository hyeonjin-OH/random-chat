package com.random.justchatting.domain.login;

import com.random.justchatting.entity.UserEntity;
import com.random.justchatting.entity.UserPreferEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPrefer {
    private Long id;
    private Long userId;
    private String preferLevel;
    private String preferRole;
    private String preferTime;

    public static UserPreferEntity toEntity(UserPrefer prefer){
        return UserPreferEntity.builder()
                .id(prefer.id)
                .userId(prefer.userId)
                .preferLevel(prefer.preferLevel)
                .preferRole(prefer.preferRole)
                .preferTime(prefer.preferTime)
                .build();
    }

    public static UserPrefer from(UserPreferEntity prefer){
        return UserPrefer.builder()
                .id(prefer.getId())
                .userId(prefer.getUserId())
                .preferLevel(prefer.getPreferLevel())
                .preferRole(prefer.getPreferRole())
                .preferTime(prefer.getPreferTime())
                .build();
    }

}
