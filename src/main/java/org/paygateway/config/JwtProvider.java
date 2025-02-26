package org.paygateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtProvider {

    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(Authentication authentication, String role) {
        String jwt = Jwts.builder().setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+JwtConstant.EXPIRATION_TIME))
                .claim("email", authentication.getName())
                .claim("authorities",role)
                .signWith(key).compact();
        return jwt;
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
        return String.valueOf(claims.get("email"));
    }
}
