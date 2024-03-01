package com.random.justchatting.repository.user;

import com.random.justchatting.entity.UserPreferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferJpaRepository extends JpaRepository<UserPreferEntity, Long> {

    UserPreferEntity findByUuId(String uId);
}
