package com.random.justchatting.service.user;

import com.random.justchatting.domain.login.User;
import com.random.justchatting.repository.user.UserPreferRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl {

    private final UserRepository userRepository;

    public Long saveApiKey(User user){

        User findUser = userRepository.findByApiKey(user.getApiKey());

        if (findUser != null){
            return findUser.getId();
        }
        else{
            User newUser = User.createUId(user);
            User savedUser = userRepository.save(newUser);
            return savedUser.getId();
        }
    }

    public String register(User user){

        User findUser = userRepository.findByApiKey(user.getApiKey());

        if (findUser != null){
            return findUser.getUuId();
        }
        else{
            User newUser = User.createUId(user);
            User savedUser = userRepository.save(newUser);
            return savedUser.getUuId();
        }
    }
}
