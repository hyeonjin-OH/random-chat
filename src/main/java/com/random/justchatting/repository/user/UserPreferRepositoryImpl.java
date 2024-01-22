package com.random.justchatting.repository.user;

import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.entity.UserPreferEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPreferRepositoryImpl implements UserPreferRepository{

    private final UserPreferJpaRepository userPreferJpaRepository;

    @Override
    public UserPrefer save(UserPrefer prefer) {
        return UserPrefer.from(userPreferJpaRepository.save(UserPrefer.toEntity(prefer)));
    }

    @Override
    public UserPrefer findByUserId(Long userid) {
        UserPreferEntity prefer = userPreferJpaRepository.findByUserId(userid);
        if(prefer != null){
            return UserPrefer.from(prefer);
        }
        else{
            return null;
        }
    }
}
