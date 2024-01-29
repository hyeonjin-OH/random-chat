package com.random.justchatting.controller.user;

import com.random.justchatting.domain.login.User;
import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.service.user.UserService;
import com.random.justchatting.service.user.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class loginController {

    private final LoginServiceImpl LoginServiceImpl;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> registerLostArk(@RequestBody User user){
        try{
            Long userId = LoginServiceImpl.saveApiKey(user);

            return ResponseEntity.ok().body(userId);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/prefer/{userId}")
    public ResponseEntity<?> registerPreference(@PathVariable Long userId){
        try{
            UserPrefer prefer = userService.getUserPreference(userId);
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

    @PostMapping("/prefer/{userId}")
    public ResponseEntity<?> registerPreference(@RequestBody UserPrefer prefer){
        try{
            UserPrefer newPrefer = userService.setUserPreference(prefer);

            return ResponseEntity.ok().body(newPrefer);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
