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


    public User register(User user){

        User findUser = userRepository.findByApiKey(user.getApiKey());

        if (findUser != null){
            return findUser;
        }
        else{
            User newUser = User.createUId(user);
            return userRepository.save(newUser);
        }
    }
}
