package com.random.justchatting.config;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

@Component
public class SHA256 {
    public SHA256() {
    }

    public String encrypt(String plainText) {

        return Hashing.sha256()
                .hashString(plainText, StandardCharsets.UTF_8)
                .toString();
    }
}