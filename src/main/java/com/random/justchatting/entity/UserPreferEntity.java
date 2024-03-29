package com.random.justchatting.entity;

import com.random.justchatting.config.IntegerListConverter;
import com.random.justchatting.config.StringListConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name="users_prefer")
@Getter
@NoArgsConstructor
public class UserPreferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuId;
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> preferRaid;
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> preferRole;
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> preferTime;

    @Builder
    public UserPreferEntity(Long id, String uuId, List<Integer> preferRaid, List<Integer> preferRole, List<Integer> preferTime) {
        this.id = id;
        this.uuId = uuId;
        this.preferRaid = preferRaid;
        this.preferRole = preferRole;
        this.preferTime = preferTime;
    }
}
