package com.random.justchatting.domain.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReq {
    public String apiKey;
    public String nickName;
    public String mainCharacter;
}
