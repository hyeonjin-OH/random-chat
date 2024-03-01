package com.random.justchatting.service.user;

import com.random.justchatting.domain.login.UserPrefer;

public interface UserService {
    UserPrefer getUserPreferenceByUId(String uId);
    UserPrefer setUserPreference(UserPrefer prefer);
}
