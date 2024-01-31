package com.random.justchatting.repository.user;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.entity.UserEntity;
import com.random.justchatting.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;
    private final ChatRoomRepository chatRoomRepository;

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

    @Override
    public List<ChatRoom> findRoomByUserId(Long userId) {

        if(userJpaRepository.findById(userId).isPresent())
        {
            User user = User.from(userJpaRepository.findById(userId).get());
            List<String> rooms = user.getRooms();

            List<ChatRoom> chatRoomList = rooms.stream().map(chatRoomRepository::findByRoomKey).toList();
            return chatRoomList;
        }

        return null;
    }

    @Override
    public List<ChatRoom> findRoomByUuId(String uId) {
        if (userJpaRepository.findByUuId(uId).isPresent()) {
            User user = User.from(userJpaRepository.findByUuId(uId).get());
            List<String> rooms = user.getRooms();

            List<ChatRoom> chatRoomList = rooms.stream().map(chatRoomRepository::findByRoomKey).toList();
            return chatRoomList;
        }

        return null;
    }
}
