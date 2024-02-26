package com.random.justchatting.exception.User;

import com.random.justchatting.auth.ErrorCode;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }

    public JwtException(ErrorCode errorCode){
        super(errorCode.getMessage());
    }

}
