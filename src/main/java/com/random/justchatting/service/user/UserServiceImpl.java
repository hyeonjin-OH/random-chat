package com.random.justchatting.service.user;

import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.repository.user.UserPreferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserPreferRepository userPreferRepository;

    @Override
    public UserPrefer getUserPreference(Long userId) {

        return userPreferRepository.findByUserId(userId);
    }


    @Override
    public UserPrefer getUserPreferenceByUId(String uId) {

        return userPreferRepository.findByUId(uId);
    }

    @Override
    public UserPrefer setUserPreference(UserPrefer prefer) {
        UserPrefer updatedPrefer = new UserPrefer(prefer.getId(),prefer.getUserId(), prefer.getUuId(), prefer.getPreferRaid(), prefer.getPreferRole(), prefer.getPreferTime());
        UserPrefer savedPrefer = userPreferRepository.findByUId(prefer.getUuId());

        if(savedPrefer != null) {
            updatedPrefer = savedPrefer.update(prefer);
            return userPreferRepository.save(updatedPrefer);
        }
        else{
            return userPreferRepository.save(updatedPrefer);
        }
    }
}
