package com.random.justchatting.controller.user;

import com.random.justchatting.Session.SessionConst;
import com.random.justchatting.auth.JwtProvider;
import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.service.user.UserService;
import com.random.justchatting.service.user.LoginServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class loginController {

    private final LoginServiceImpl LoginServiceImpl;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerLostArk(@RequestBody User user, HttpSession session){
        try{
            User savedUser = LoginServiceImpl.register(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getUuId(), savedUser.getApiKey());
            List<GrantedAuthority> authoritiesForUser = new ArrayList<>();
            String accessToken = jwtProvider.createAccessToken(savedUser.getUuId(),authoritiesForUser);
            String refreshToken = jwtProvider.createRefreshToken(authentication, authoritiesForUser);

            HashMap<String, String> token = new HashMap<>();
            token.put("accessToken", accessToken);
            token.put("refreshToken", refreshToken);

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
    public ResponseEntity<?> reissueToken(@AuthenticationPrincipal UserDetails userDetails){
        String uuId = userDetails.getUsername();
        List<GrantedAuthority> authorities = new ArrayList<>();
        String newAccessToken = jwtProvider.createAccessToken(uuId, authorities);

        return ResponseEntity.ok().body(newAccessToken);
    }
}
