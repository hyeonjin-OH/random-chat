package com.random.justchatting.config;

import com.random.justchatting.domain.login.User;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String apikey) throws UsernameNotFoundException {
        User user = userRepository.findByApiKey(apikey);

        if(user == null){
            throw new UsernameNotFoundException("해당 사용자가 없습니다.");
        }
        else{
            return new PrincipalDetails(user);
        }
    }
}
