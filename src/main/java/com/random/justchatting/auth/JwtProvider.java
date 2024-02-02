package com.random.justchatting.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.access.expiration}")
    private long accessTokenTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenTime;

    private Key key;

    public JwtProvider(
            @Value("${jwt.secret_key}") String secret_key
    ) {
        byte[] secretByteKey = Decoders.BASE64.decode(secret_key);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public String createRefreshToken(Authentication authentication,
                                List<GrantedAuthority> authorities) {

        log.info("authentication in JwtProvider : " + authentication);

        // 권한 가져오기
        // authentication객체에서 권한 정보(GrantedAuthority)를 가져와 문자열로 변환
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        // 클레임에 "sub"라는 key로 등록해줌
        claims.put("sub", authentication.getName());

        // claims in JwtProvider : {auth=[ROLE_USER]}
        log.info("claims in JwtProvider : " + claims);
        // authentication.getName() in JwtProvider : zxzz45@naver.com
        log.info("authentication.getName() in JwtProvider : " + authentication.getName());

        // JWT 시간 설정
        long now = (new Date()).getTime();
        Date now2 = new Date();

        // RefreshToken 생성
        Date refreshTokenExpire = new Date(now + this.refreshTokenTime);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now2)
                .setExpiration(refreshTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info(checkToken(refreshToken));

      return refreshToken;
    }


    // accessToken 만료시 refreshToken으로 accessToken 발급
    public String createAccessToken(String uuId, List<GrantedAuthority> authorities) {
        Long now = (new Date()).getTime();
        Date now2 = new Date();
        Date accessTokenExpire = new Date(now + this.accessTokenTime);

        log.info("authorities : " + authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        // setSubject이다.
        // 클레임에 subject를 넣는것
        claims.put("sub", uuId);

        log.info("claims : " + claims);

        String accessToken = Jwts.builder()
                .setIssuedAt(now2)
                .setClaims(claims)
                .setExpiration(accessTokenExpire)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("accessToken in JwtProvider : " + accessToken);

        // claims subject 확인 in JwtProvider : zxzz45@naver.com
        log.info("claims subject 확인 in JwtProvider : " + checkToken(accessToken));

        return accessToken;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 코드
    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        // 토큰 복호화 메소드
        Claims claims = parseClaims(token);
        log.info("claims : " + claims);

        if(claims.get("auth") == null) {
            log.info("권한 정보가 없는 토큰입니다.");
        }
        // 권한 정보 가져오기
        List<String> authority = (List<String>) claims.get(AUTHORITIES_KEY);
        log.info("authority : " + authority);

        Collection<? extends GrantedAuthority> authorities =
                authority.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        log.info("subject : " + claims.getSubject());

        // 일반 로그인 시 주로 이거로 인증처리해서 SecurityContext에 저장한다.
        // Spring Security에서 인증을 나타내는 객체로 사용됩니다.
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // 토큰 복호화 메소드
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException : " + e.getMessage());
            log.info("ExpiredJwtException : " + e.getClaims());
            return e.getClaims();
        }
    }

    // 토큰을 만들 때 제대로 만들어졌는지 log를 찍어보려고할 때
    // 토큰을 만들 때마다 치면 가독성이 떨어지니
    // 메소드로 만들어줍니다.
    private String checkToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject();
        return subject;
    }

    // 토큰 검증을 위해 사용
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 설명입니다. \n info : " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT입니다. \n info : " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT입니다. \n info : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT가 잘못되었습니다. \n info : " + e.getMessage());
        }
        return false;
    }

}