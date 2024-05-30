package com.example.waytogo.security.jwt;

import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import com.example.waytogo.user.service.api.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JWTServiceImpl implements JWTService {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 3600000;
    private final UserRepository userRepository;

    @Override
    public String generateJwt(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        UUID userId = userRepository.findByUsername(username).get().getId();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .claim("userId", userId)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Authentication validateJwt(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            if (username == null) {
                return null;
            }

            String userId = claims.get("userId", String.class);
            if (userId == null) {
                return null;
            }

            Optional<User> userDetails = userRepository.findByUsername(username);
            return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (JwtException | IllegalArgumentException e) {
            // Log the exception and return null if the token is invalid
            e.printStackTrace();
            return null;
        }
    }
}
