package com.random.justchatting.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.random.justchatting.Session.SessionConst;
import com.random.justchatting.auth.JwtProvider;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.exception.User.JwtException;
import com.random.justchatting.service.user.UserService;
import com.random.justchatting.service.user.LoginServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class loginController {

    private final LoginServiceImpl LoginServiceImpl;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseEntity<?> registerLostArk(@RequestBody User user, HttpServletResponse response){
        try{
            User savedUser = LoginServiceImpl.register(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getUuId(), savedUser.getApiKey());
            List<GrantedAuthority> authoritiesForUser = new ArrayList<>();
            String accessToken = jwtProvider.createAccessToken(savedUser.getUuId());
            String refreshToken = jwtProvider.createRefreshToken(authentication, authoritiesForUser);

            HashMap<String, String> token = new HashMap<>();
            token.put("accessToken", accessToken);
            //token.put("refreshToken", refreshToken);

            // create a cookie
            Cookie cookie = new Cookie("refresh",refreshToken);

            // expires in 31 days
            cookie.setMaxAge(31 * 24 * 60 * 60);

            // optional properties
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);

            return ResponseEntity.ok().body(token);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/v1/prefer")
    public ResponseEntity<?> registerPreference(@AuthenticationPrincipal UserDetails user, HttpServletRequest request){
        try{
            String userId = user.getUsername();
            UserPrefer prefer = userService.getUserPreferenceByUId(userId);
            if (prefer != null){
                return ResponseEntity.ok().body(prefer);
            }
            else{
                return ResponseEntity.ok().body(null);
            }

        }catch (Exception e){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/v1/register"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
    }

    @PostMapping("/api/v1/prefer")
    public ResponseEntity<?> registerPreference(@AuthenticationPrincipal UserDetails user, @RequestBody UserPrefer prefer){
        try{
            String uuId = user.getUsername();
            prefer.setUuId(uuId);
            UserPrefer newPrefer = userService.setUserPreference(prefer);

            return ResponseEntity.ok().body(newPrefer);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request,
                                          HttpServletResponse response){
        try{
            String refresh = "";
            Cookie[] requestCookies = request.getCookies();
            if(requestCookies != null){
                for(Cookie c : requestCookies){
                    if(c.getName().equals("refresh")){
                        refresh = c.getValue();
                    }
                }
            }
            String newAccessToken = jwtProvider.reissueAccessToken(refresh);

            HashMap<String, String> token = new HashMap<>();
            token.put("accessToken", newAccessToken);

            return ResponseEntity.ok().body(token);
        }catch(JwtException e){
            if(Objects.equals(e.getMessage(), "RefreshToken EXPIRED")) {
                Authentication authentication = jwtProvider.getAuthentication(jwtProvider.resolveToken(request));
                List<GrantedAuthority> authoritiesForUser = new ArrayList<>();
                String refreshToken = jwtProvider.createRefreshToken(authentication, authoritiesForUser);
                String newAccessToken = jwtProvider.reissueAccessToken(refreshToken);

                HashMap<String, String> token = new HashMap<>();
                token.put("accessToken", newAccessToken);
                //token.put("refreshToken", refreshToken);

                // create a cookie
                Cookie cookie = new Cookie("refresh",refreshToken);

                // expires in 30 days
                cookie.setMaxAge(31 * 24 * 60 * 60);
                // optional properties
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);

                return ResponseEntity.ok().body(token);
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token재발급 중 에러가 발생하였습니다.");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken이 유효하지 않습니다.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){

        Cookie[] requestCookies = request.getCookies();
        for(int i=0; i< requestCookies.length; i++) {
            //if(requestCookies[i].getName().equals("refresh") || requestCookies[i].getName().equals("accessToken"))
            requestCookies[i].setMaxAge(0); // 유효시간을 0으로 설정
            response.addCookie(requestCookies[i]); // 응답 헤더에 추가
        }
        return ResponseEntity.ok().body("로그아웃되었습니다.");
    }


}
