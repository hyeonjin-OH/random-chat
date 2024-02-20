package com.random.justchatting.service.user;

import com.random.justchatting.config.SHA256;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.repository.user.UserPreferRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl {

    private final UserRepository userRepository;
    private final SHA256 encoder;


    @Transactional
    public User register(User user){

        // APIkey는 민감 정보x, 복호화는 못하더라도 비교는 가능해야 하기에 SHA-256알고리즘만 사용
        String encodedApiKey = encoder.encrypt(user.getApiKey());
        User findUser = userRepository.findByApiKey(encodedApiKey);

        if (findUser != null){
            return findUser;
        }
        else{
            User newUser = User.createUId(user);
            newUser.encodeApiKey(encodedApiKey);

            return userRepository.save(newUser);
        }
    }
}
