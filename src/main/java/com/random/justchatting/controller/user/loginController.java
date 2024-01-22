package com.random.justchatting.controller.user;

import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.domain.login.UserReq;
import com.random.justchatting.service.user.loginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class loginController {

    private final loginService loginService;

    @PostMapping("/register")
    public ResponseEntity<?> registerLostArk(User user){
        try{
            Long userId = loginService.saveApiKey(user);

            return ResponseEntity.ok().body(userId);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/prefer/{userId}")
    public ResponseEntity<?> registerPreference(@PathVariable Long userId){
        try{
            UserPrefer prefer = loginService.getPreference(userId);
            if (prefer != null){
                return ResponseEntity.ok().body(prefer);
            }
            else{
                return ResponseEntity.ok().body(null);
            }

        }catch (Exception e){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
    }

    @PostMapping("/prefer/{userId}")
    public ResponseEntity<?> registerPreference(UserPrefer prefer){
        try{
            Long userId = loginService.savePreference(prefer);

            return ResponseEntity.ok().body(userId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
