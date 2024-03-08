package com.random.justchatting.domain.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MatchReq {

    private String uuId;
    private String prefer;
    private int optionCount;
    private String roomKey;
    private long time;
}
