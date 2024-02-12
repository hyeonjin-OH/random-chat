package com.random.justchatting.entity;

import com.random.justchatting.config.StringListConverter;
import com.random.justchatting.domain.login.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name="Users")
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length=1000)
    private String apiKey;
    private String uuId;
    private String nickName;
    @Convert(converter = StringListConverter.class)
    private List<String> rooms=new ArrayList<>();

    @Builder
    public UserEntity(Long id, String apiKey, String uuId, String nickName, List<String> rooms) {
        this.id = id;
        this.apiKey = apiKey;
        this.uuId = uuId;
        this.nickName = nickName;
        this.rooms = rooms;
    }
}
