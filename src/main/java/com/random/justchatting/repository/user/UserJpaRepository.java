package com.random.justchatting.repository.user;

import com.random.justchatting.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByApiKey(String apiKey);

    Optional<UserEntity> findByUuId(String uId);

}
