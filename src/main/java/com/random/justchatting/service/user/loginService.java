package com.random.justchatting.service.user;

import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.domain.login.UserReq;
import com.random.justchatting.repository.user.UserPreferRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class loginService {

    private final UserRepository userRepository;
    private final UserPreferRepository userPreferRepository;

    public Long saveApiKey(User user){

        User findUser = userRepository.findByApiKey(user.getApiKey());

        if (findUser != null){
            return findUser.getId();
        }
        else{
            User newUser = userRepository.save(user);
            return newUser.getId();
        }
    }

    public UserPrefer getPreference(Long userId){

        return userPreferRepository.findByUserId(userId);
    }

    public Long savePreference(UserPrefer prefer){
        UserPrefer newPrefer = userPreferRepository.save(prefer);

        return newPrefer.getUserId();
    }

}
