package com.random.justchatting.controller.chat;

import com.random.justchatting.domain.login.UserPrefer;
import com.random.justchatting.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private final UserService userService;

    @GetMapping("/waitingroom/{userId}")
    public ResponseEntity<?> getWaitingRoom(@PathVariable Long userId){
        UserPrefer prefer = userService.getUserPreference(userId);
        if (prefer != null){
            return ResponseEntity.ok().body(prefer);
        }
        else{
            return ResponseEntity.ok().body(null);
        }
    }

    @PostMapping("/waitingroom/{userId}")
    public ResponseEntity<?> findRoom(@PathVariable Long userId, @RequestBody UserPrefer prefer){

        return ResponseEntity.ok().body(null);
    }


}
