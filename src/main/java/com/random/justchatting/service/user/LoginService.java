package com.random.justchatting.service.user;

import com.random.justchatting.domain.login.User;

public interface LoginService {

    Long saveApiKey(User user);

    String register(User user);
}
