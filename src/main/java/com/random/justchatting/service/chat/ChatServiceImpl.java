package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final UserRepository userRepository;

    private Map<String, ChatRoom> chatRooms;


    public List<ChatRoom> findAllRoom(String uId){

        return userRepository.findRoomByUuId(uId);
    }
}
