package com.random.justchatting.service.chat;

import com.random.justchatting.domain.chat.ChatMessages;
import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.exception.Chat.ChatException;
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

        try{
            return userRepository.findRoomByUuId(uId);
        }catch (NullPointerException e){
            return null;
        }

    }

    public List<ChatMessages> findAllMessages(String roomKey){
        return chatRepository.getAllMessages(roomKey);
    }

    public String findRoomKeyByRoomIdAndUuId(Long roomId, String uuId){
        return chatRoomRepository.findRoomKeyByRoomIdAndUuId(roomId, uuId).getRoomKey();
    }

    public String findRoomKeyByRoomId(Long roomId){
        return  chatRoomRepository.findRoomByRoomId(roomId).getRoomKey();
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

        return chatRoomRepository.saveRoom(room);
    }

    @Override
    public ChatRoom findRoomInfo(String roomKey) {
        ChatRoom room = null;
        room = chatRoomRepository.findByRoomKey(roomKey);
        return room;
    }

    // 채팅 목록 및 방 모두 삭제
    @Transactional
    public void deleteChatRoom(String roomKey) {
        try{
            // 두 사람 모두 퇴장했다면 대화내용 및 방 삭제
            ChatRoom reloadRoom =chatRoomRepository.findByRoomKey(roomKey);
            if(reloadRoom.getSenderId().equals("") && reloadRoom.getReceiverId().equals("")) {
                chatRoomRepository.deleteRoom(chatRoomRepository.findByRoomKey(roomKey));
                chatRepository.deleteAllMessages(roomKey);
            }
        }catch (Exception e){
            throw new ChatException("채팅방을 삭제하는 중에 문제가 발생하였습니다.");
        }

    }


}
