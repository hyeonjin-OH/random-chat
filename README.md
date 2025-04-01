# lo-chat
### lostark 깐부찾기 랜덤채팅
<br><br>
## FrameWork & Library
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/React Native-61DAFB?style=flat-square&logo=React&logoColor=black"/> <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/socketJs-010101?style=for-the-badge&logo=socketdotio&logoColor=white"> <img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/jwt-97979A?style=for-the-badge&logo=&logoColor=white">
<br><br><br><br>

## INTRODUCE
### 매칭서비스 (Redis & WebSocket + STOMP)
1. 매칭 옵션은 여러 옵션들 중 최대 3개까지 선택한다.
2. 기본적으로 3개가 모두 동일한 경우에만 매칭을 시켜준다.
3. 단, 20초가 경과하도록 매칭이 되지 않는다면 '계속 대기' or '선택옵션 -1개로 재매 칭'을 선택할 수 있다. 
4. 매칭이 된다면 채팅방을 생성하며 양 측이 동시 입장하게 된다.

### 채팅서비스 (WebSocket + STOMP)
1. 입장하는 순간에 소켓 통신이 이루어지며 실시간으로 채팅이 가능하다.
2. 채팅 내용은 모두 저장된다. 랜덤 매칭이지만 다음에 다시 접속했을 때도 계속 채팅을 이어갈 수 있게 하기 위함이다.
3. 한 명이 방을 퇴장하면 퇴장 안내문구가 뜨며, 두 사람이 모두 퇴장하면 방은 삭제된다.
     한 명이 퇴장을 하면 남은 사람은 채팅방에서 채팅은 불가능하다. 내용만 확인 가능하다.

<br>
<br>

## 각 서비스 구현 방식

### 채팅서비스 
https://blog.naver.com/mion0602/223357633094

### 매칭서비스 
https://blog.naver.com/mion0602/223358442873
