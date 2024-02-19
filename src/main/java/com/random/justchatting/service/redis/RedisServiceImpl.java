package com.random.justchatting.service.redis;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.match.MatchReq;
import com.random.justchatting.exception.Chat.MatchException;
import com.random.justchatting.repository.chat.ChatRoomRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 사용자가 옵션을 선택할 때마다 Redis에 사용자 ID를 해당 옵션 조합 집합에 추가

    @Transactional
    public ChatRoom addUserOptions(long timeMillis, MatchReq req) {
        ChatRoom room = setRoomInfo(req);
        redisTemplate.opsForZSet().add(Integer.toString(req.getOptionCount())+":"+ req.getPrefer(),
                                req.getUuId()+ ":" + room.getRoomKey(), timeMillis);

        return room;
    }

    @Transactional
    public ChatRoom modifyUserOptions(MatchReq req) {

        ChatRoom room = chatRoomRepository.findByRoomKey(req.getRoomKey());

        redisTemplate.opsForZSet().remove(Integer.toString(req.getOptionCount()+1)+":"+ req.getPrefer(), req.getUuId()+ ":" + req.getRoomKey());
        redisTemplate.opsForZSet().add(Integer.toString(req.getOptionCount())+":"+ req.getPrefer(),
                req.getUuId() + ":" + req.getRoomKey(), req.getTime());

        return room;
    }


    // 매칭을 수행하는 메소드
    @Transactional
    public String preferMatching(MatchReq req) {
        int positionIdx = req.getPrefer().indexOf("-");

        // 매칭 시, prefer 중 포지션 부분은 서로 반대되는 것 끼리 매칭시켜줘야 함
        // position은 둘 중 하나임.
        String position = req.getPrefer().substring(positionIdx + 1).equals("101") ? "100" : "101";
        String prefer = Integer.toString(req.getOptionCount())+":"+ req.getPrefer().substring(0, positionIdx) + "-" + position;
        int preferCount = req.getPrefer().split(",").length;

        Set<String> user = redisTemplate.opsForZSet().range(prefer, 0, 0);

        // 매칭 prefer과 매칭되는 사용자가 있다면 방 입장 및 redis 대기열에서 삭제
        if(user !=null && user.size()> 0){
            String matchUser = user.iterator().next();
            // ':'를 기준으로 분리하여 value 가져오기
            String[] parts = matchUser.split(":");
            String uuId = "";
            String roomKey = "";
            if (parts.length >= 2) {
                uuId = parts[0];
                roomKey = parts[1];
            }
            enterRoomInfo(req, uuId, roomKey);
            redisTemplate.opsForZSet().remove(prefer, uuId + ":" + roomKey);

            return roomKey;
        }
        else if(user.size()== 0){
            // 옵션 전체 일치 아닐 시 옵션개수 기준으로 비교
            ScanOptions scanOptions = ScanOptions.scanOptions().match(req.getOptionCount()+":*").build();
            Cursor<byte[]> keys = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);

            if (keys.hasNext()) {
                return checkOptions2(keys, req);
            }

        }
        return "";
    }

    private String checkOptions2(Cursor<byte[]> keys, MatchReq req) {

        while (keys.hasNext()) {
            String key = new String(keys.next());

            // 순차적으로 해당 key를 가진 user의 value도 가져오기 위함
            Set<String> user = redisTemplate.opsForZSet().reverseRange(key, 0, 0);
            String matchUser = user.iterator().next();
            // ':'를 기준으로 분리하여 value 가져오기
            String[] parts = matchUser.split(":");
            String cmpUuId = "";
            String cmpRoomKey = "";
            if (parts.length >= 2) {
                cmpUuId = parts[0];
                cmpRoomKey = parts[1];
            }

            // 본인이라면 패쓰
            if (cmpUuId.equals(req.getUuId())) {
                continue;
            }
            // 타인이라면 선호 비교
            else {
                int cmpPositionIdx = key.indexOf("-");
                String[] cmpPrefers = key.substring(key.indexOf(":") + 1, cmpPositionIdx).split(",");
                int myPositionIdx = req.getPrefer().indexOf("-");
                String[] myPrefers = req.getPrefer().substring(0, myPositionIdx).split(",");

                List<String> tmpList = new ArrayList<>(Arrays.asList(cmpPrefers));

                int matchCount = 0;
                for (String p : myPrefers) {
                    matchCount = tmpList.contains(p) ? matchCount + 1 : matchCount;
                }

                if (matchCount >= req.getOptionCount()) {
                    enterRoomInfo(req, cmpUuId, cmpRoomKey);
                    redisTemplate.opsForZSet().remove(key, matchUser);
                    redisTemplate.opsForZSet().remove(Integer.toString(req.getOptionCount())+":"+ req.getPrefer(), req.getUuId()+ ":" + req.getRoomKey());
                    return cmpRoomKey;

                }
            }
        }
        return "";
    }

    @Override
    @Transactional
    public void cancelMatch(MatchReq req) {
        try{
            User user = userRepository.findByUuId(req.getUuId());
            user.exitRoom(req.getRoomKey());
            userRepository.save(user);

            // Redis에서 찾는 중인 경우에는 roomKey가 없을 수도 있기 때문에
            if(!Objects.equals(req.getRoomKey(), "")){
                ChatRoom room =  chatRoomRepository.findByRoomKey(req.getRoomKey());
                chatRoomRepository.deleteRoom(room);
            }

            redisTemplate.opsForZSet().remove(Integer.toString(req.getOptionCount())+":"+ req.getPrefer(), req.getUuId()+":"+req.getRoomKey());
        }catch (MatchException e){
            throw new MatchException("매칭 취소하는 중 에러가 발생하였습니다.");
        }

    }

    private ChatRoom setRoomInfo(MatchReq req){
        User user = userRepository.findByUuId(req.getUuId());
        ChatRoom room = ChatRoom.create();
        room.setSender(req.getUuId());
        chatRoomRepository.saveRoom(room);
        user.enterRoom(room.getRoomKey());
        userRepository.save(user);

        return room;
    }

    /*
    * uuId : 매칭 대상의 uuId
    * roomKey : 매칭 된 roomKey
    */
    private void enterRoomInfo(MatchReq req, String uuId, String roomKey){

        // 기존에 본인이 개설한 방이 있다면
        if(!req.getRoomKey().equals("")){
            // 기존 본인 방 삭제
            chatRoomRepository.deleteRoom(chatRoomRepository.findByRoomKey(req.getRoomKey()));
            // 본인 정보 업데이트
            User user = userRepository.findByUuId(req.getUuId());
            user.exitRoom(req.getRoomKey());
            userRepository.save(user);
        }

        // 매칭 대상자 방으로 정보 업데이트
        ChatRoom room = chatRoomRepository.findByRoomKey(roomKey);
        room.setReceiver(req.getUuId());
        chatRoomRepository.saveRoom(room);
        User user = userRepository.findByUuId(req.getUuId());
        user.enterRoom(roomKey);
        userRepository.save(user);
    }

}
