package com.random.justchatting.repository.user;

import com.random.justchatting.domain.login.User;

public interface UserRepository {

    User save(User user);

    User findByApiKey(String apiKey);
}
