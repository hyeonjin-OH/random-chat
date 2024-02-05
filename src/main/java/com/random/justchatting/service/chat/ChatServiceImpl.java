package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.repository.chat.ChatRepository;
import com.random.justchatting.repository.chat.ChatRoomRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{

    private final UserRepository userRepository;
    private final ChatRepository  chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    private Map<String, ChatRoom> chatRooms;


    public List<ChatRoom> findAllRoom(String uId){

        return userRepository.findRoomByUuId(uId);
    }

    public List<ChatMessages> findAllMessages(String roomKey){
        return chatRepository.getAllMessages(roomKey);
    }

    public String findRoomKeyByRoomIdAndUuId(Long roomId, String uuId){
        return chatRoomRepository.findRoomKeyByRoomIdAndUuId(roomId, uuId).getRoomKey();
    }
}
