package com.random.justchatting.repository.user;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User findByApiKey(String apiKey);

    List<ChatRoom> findRoomByUserId(Long userId);


    List<ChatRoom> findRoomByUuId(String uId);
}
