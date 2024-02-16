package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.exception.User.UserException;
import com.random.justchatting.repository.chat.ChatRepository;
import com.random.justchatting.repository.chat.ChatRoomRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    @Transactional
    public ChatRoom openChatRoom(String senderId) {
        ChatRoom room = ChatRoom.create();
        room.setSender(senderId);

        User user = userRepository.findByUuId(senderId);
        user.enterRoom(room.getRoomKey());
        userRepository.save(user);

        return chatRoomRepository.saveRoom(room);
    }

    @Override
    @Transactional
    public ChatRoom enterChatRoom(String receiverId, String roomKey) {
        ChatRoom room = chatRoomRepository.findByRoomKey(roomKey);
        room.setReceiver(receiverId);

        User user = userRepository.findByUuId(receiverId);
        user.enterRoom(roomKey);
        userRepository.save(user);

        return chatRoomRepository.saveRoom(room);
    }

    @Override
    @Transactional
    public ChatRoom exitChatRoom(String roomKey, String uuId) {
        ChatRoom room = chatRoomRepository.findByRoomKey(roomKey);

        if(room.getSenderId().equals(uuId)){
            room.setSender("");
        }
        else if(room.getReceiverId().equals(uuId)){
            room.setReceiver("");
        }
        else{
            throw new UserException("해당 ID는 해당 방에 존재하는 ID가 아닙니다.");
        }

        User user = userRepository.findByUuId(uuId);
        user.exitRoom(roomKey);
        userRepository.save(user);

        // 두 사람 모두 퇴장했다면 대화내용 및 방 삭제
        ChatRoom reloadRoom =chatRoomRepository.findByRoomKey(roomKey);
        if(reloadRoom.getSenderId().equals("")
            && reloadRoom.getReceiverId().equals("")){
            deleteChatRoom(roomKey);
        }

        return chatRoomRepository.saveRoom(room);
    }

    @Override
    public ChatRoom findRoomInfo(String roomKey) {
        return chatRoomRepository.findByRoomKey(roomKey);
    }

    // 채팅 목록 및 방 모두 삭제
    private void deleteChatRoom(String roomKey) {

        chatRoomRepository.deleteRoom(chatRoomRepository.findByRoomKey(roomKey));
        chatRepository.deleteAllMessages(roomKey);
    }


}
