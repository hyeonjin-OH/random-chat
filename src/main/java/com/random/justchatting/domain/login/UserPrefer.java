package com.random.justchatting.domain.login;

import com.random.justchatting.entity.UserPreferEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserPrefer {
    private Long id;
    private Long userId;
    private List<Integer> preferRaid  = new ArrayList<>();
    private List<Integer> preferRole  = new ArrayList<>();
    private List<Integer> preferTime = new ArrayList<>();

    @Builder
    public UserPrefer(Long id, Long userId, List<Integer> preferRaid, List<Integer> preferRole, List<Integer> preferTime) {
        this.id = id;
        this.userId = userId;
        this.preferRaid = preferRaid;
        this.preferRole = preferRole;
        this.preferTime = preferTime;
    }

    public static UserPreferEntity toEntity(UserPrefer prefer){
        return UserPreferEntity.builder()
                .id(prefer.id)
                .userId(prefer.userId)
                .preferRaid(prefer.preferRaid)
                .preferRole(prefer.preferRole)
                .preferTime(prefer.preferTime)
                .build();
    }

    public static UserPrefer from(UserPreferEntity prefer){
        return UserPrefer.builder()
                .id(prefer.getId())
                .userId(prefer.getUserId())
                .preferRaid(prefer.getPreferRaid())
                .preferRole(prefer.getPreferRole())
                .preferTime(prefer.getPreferTime())
                .build();
    }

    public UserPrefer update(UserPrefer newPrefer){
        return UserPrefer.builder()
                .id(this.id)
                .userId(this.userId)
                .preferRaid(newPrefer.preferRaid)
                .preferRole(newPrefer.preferRole)
                .preferTime(newPrefer.preferTime)
                .build();
    }

}
