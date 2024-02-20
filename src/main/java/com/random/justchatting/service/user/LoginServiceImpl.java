package com.random.justchatting.service.user;

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
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User register(User user){

        String encodedApiKey = passwordEncoder.encode(user.getApiKey());
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
