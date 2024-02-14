package com.random.justchatting.service.redis;

import com.random.justchatting.domain.chat.ChatRoom;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.match.MatchReq;
import com.random.justchatting.repository.chat.ChatRoomRepository;
import com.random.justchatting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.zset.Tuple;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
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

        redisTemplate.opsForZSet().add(req.getPrefer(),
                                req.getUuId(), timeMillis);

        ChatRoom room = setRoomInfo(req);
        redisTemplate.opsForZSet().remove(req.getPrefer(), req.getUuId());
        redisTemplate.opsForZSet().add(req.getPrefer(),
                req.getUuId() + ":" + room.getRoomKey(), timeMillis);

        return room;
    }

    // 매칭을 수행하는 메소드
    @Transactional
    public String preferMatching(MatchReq req) {
        int positionIdx = req.getPrefer().indexOf("-");

        // 매칭 시, prefer 중 포지션 부분은 서로 반대되는 것 끼리 매칭시켜줘야 함
        // position은 둘 중 하나임.
        String position = req.getPrefer().substring(positionIdx + 1).equals("101") ? "100" : "101";
        String prefer = req.getPrefer().substring(0, positionIdx) + "-" + position;

        Set<String> user = redisTemplate.opsForZSet().reverseRange(prefer, 0, 0);

        // 매칭 prefer과 매칭되는 사용자가 있다면 알고리즘 수행
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
            return "";
        }
        else{
            // 옵션 전체 일치 아닐 시
            if (req.getOptionCount() != 3){
                Cursor<ZSetOperations.TypedTuple<String>> cursor = redisTemplate.opsForZSet().scan(prefer, ScanOptions.NONE);

                while (cursor.hasNext()) {
                    return checkOptions(req, cursor, positionIdx);
                }
            }
        }
        return "";
    }

    @Override
    public void cancelMatch(MatchReq req) {

        redisTemplate.opsForZSet().remove(req.getPrefer(), req.getUuId()+"-"+req.getRoomKey());
    }

    // 선호 옵션이 req.getOptionCount만큼 일치한다면 해당 roomKey반환
    private String checkOptions(MatchReq req, Cursor<ZSetOperations.TypedTuple<String>> cursor, int positionIdx){
        ZSetOperations.TypedTuple<String> tuple = cursor.next();
        String member = tuple.getValue(); // 멤버를 가져옴

        // 멤버에서 키 값을 추출
        String[] parts = member.split(":"); // ":"를 기준으로 멤버를 분해
        String key = parts[0]; // 분해된 멤버의 첫 번째 부분이 키 값
        String value = parts[1];

        String[] raidPrefers = req.getPrefer().substring(0, positionIdx-1).split(",");
        String[] cmpPrefers = key.split(",");
        List<String> tmpList = new ArrayList<>(Arrays.asList(cmpPrefers));

        int matchCount = 0;
        for(String p : raidPrefers){
            matchCount = tmpList.contains(p) ? matchCount +1 : matchCount;
        }

        if(matchCount >= req.getOptionCount()){
            redisTemplate.opsForZSet().remove(key, value);
            return value.split(":")[1];
        }

        return "";
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
        ChatRoom room = chatRoomRepository.findByRoomKey(roomKey);
        room.setReceiver(req.getUuId());
        chatRoomRepository.saveRoom(room);
        User user = userRepository.findByUuId(req.getUuId());
        user.enterRoom(roomKey);
        userRepository.save(user);
    }


}
