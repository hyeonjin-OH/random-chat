package com.random.justchatting.repository.user;

import com.random.justchatting.domain.login.UserPrefer;

public interface UserPreferRepository {

    UserPrefer save(UserPrefer prefer);

     UserPrefer findByUId(String uId);
}
