package com.random.justchatting.repository.user;

import com.random.justchatting.domain.login.User;
import com.random.justchatting.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByApiKey(String apiKey);
}
