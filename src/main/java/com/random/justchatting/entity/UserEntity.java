package com.random.justchatting.entity;

import com.random.justchatting.domain.login.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="Users")
@Getter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length=1000)
    private String apiKey;
    private String nickName;
    private String mainCharacter;

}
