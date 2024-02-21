package com.random.justchatting.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        // request에서 JWT를 추출
        // 요청 헤더에서 토큰을 추출하는 역할
        String token = resolveToken(httpServletRequest);
        log.info("token : " + token);

        if(StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            // 여기까지 통과하면 토큰은 인증도 받았고 권한도 있다.
            Authentication authentication = jwtProvider.getAuthentication(token);
            // Spring Security의 SecurityContextHolder를 사용하여 현재 인증 정보를 설정합니다.
            // 이를 통해 현재 사용자가 인증된 상태로 처리됩니다.
            // 이렇게 저장하면 컨트롤러에서 토큰에서 정보를 가져와서 사용할 수 있습니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.error("유효한 토큰이 없습니다.");
        }
        filterChain.doFilter(request, response);
    }

    // 토큰을 가져오기 위한 메소드
    // Authorization로 정의된 헤더 이름을 사용하여 토큰을 찾고
    // 토큰이 "Bearer "로 시작하거나 "Bearer "로 안온 것도 토큰 반환
    private String resolveToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HEADER_AUTHORIZATION);

        // 토큰이 포함하거나 Bearer 로 시작하면 true
        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else if(StringUtils.hasText(token)) {
            return token;
        } else {
            return null;
        }
    }
}