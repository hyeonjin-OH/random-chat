package com.random.justchatting.service.redis;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.match.MatchReq;

public interface RedisService {

    ChatRoom addUserOptions(long time, MatchReq req);

    ChatRoom modifyUserOptions(MatchReq req);

    String preferMatching(MatchReq req);


    void cancelMatch(MatchReq req);

}
