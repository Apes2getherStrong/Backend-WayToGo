package com.example.waytogo.security.jwt;

import org.springframework.security.core.Authentication;

import java.text.ParseException;

public interface JWTService {
    String generateJwt(String username) throws ParseException;
    Authentication validateJwt(String jwt);
}
