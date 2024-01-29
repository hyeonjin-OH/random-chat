package com.random.justchatting.service.user;

import com.random.justchatting.domain.login.UserPrefer;

public interface UserService {

    UserPrefer getUserPreference(Long userId);
    UserPrefer setUserPreference(UserPrefer prefer);
}
