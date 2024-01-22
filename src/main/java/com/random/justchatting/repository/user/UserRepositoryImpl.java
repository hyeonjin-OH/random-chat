package com.random.justchatting.repository.user;

import com.random.justchatting.domain.login.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return User.from(userJpaRepository.save(User.toEntity(user)));
    }

    @Override
    public User findByApiKey(String apiKey) {
        if (userJpaRepository.findByApiKey(apiKey) != null){
            return User.from(userJpaRepository.findByApiKey(apiKey));
        }
        else{
            return null;
        }
    }
}
