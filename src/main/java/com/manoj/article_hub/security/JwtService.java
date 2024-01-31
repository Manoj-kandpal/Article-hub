package com.manoj.article_hub.security;

import com.manoj.article_hub.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    // todo: put secret key into properties of .env file

    private static final String SECRET_KEY = "7g7B0LDyFo0couV9qw6Gaw8JLg0O9ayV4hcZ1vMFX17frdyDmqBaZUBspb7M8imC";
    private static final String COOKIE_NAME = "token";
    private static final int COOKIE_VALIDITY = 24 * 60 * 60;
    private static final int TOKEN_EXPIRY_VALIDITY_MILLISECONDS = 24*60*60*1000;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserEntity userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateTokenWithNameAndEmail(UserEntity userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("name", userDetails.getFirstName());
        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserEntity userDetails) {
        return Jwts.builder()
                .addClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY_VALIDITY_MILLISECONDS))
                .signWith(getSignInKey())
                .compact();
    }

    public Cookie generateCookieWithJwt(UserEntity userDetails) {
        String token = generateTokenWithNameAndEmail(userDetails);
        Cookie jwtCookie = new Cookie(COOKIE_NAME, token);
        jwtCookie.setMaxAge(COOKIE_VALIDITY);
        return jwtCookie;
    }

    public String getJwtTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
