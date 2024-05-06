package com.smartjob.user.util;

import com.smartjob.user.dto.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.*;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "f6be8fb4438c11eebe560242ac120002";

    private final static String REFRESH_TOKEN_SECRET = "d6bi6fb3354c22eubs560242dd140002";

    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 180L;
    public final static Long REFRESH_TOKEN_VALIDITY_SECONDS = 3600L * 24L * 7L;


    public static JwtResponse createToken(String name, String email) {

        long accessTokenExpirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date accessTokenExpirationDate = new Date(System.currentTimeMillis() + accessTokenExpirationTime);

        Map<String, Object> accessTokenPayload = new HashMap<>();
        accessTokenPayload.put("name", name);

        String accessToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(accessTokenExpirationDate)
                .addClaims(accessTokenPayload)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
                .compact();

        long refreshTokenExpirationTime = REFRESH_TOKEN_VALIDITY_SECONDS * 1000;
        Date refreshTokenExpirationDate = new Date(System.currentTimeMillis() + refreshTokenExpirationTime);

        Map<String, Object> refreshTokenPayload = new HashMap<>();
        refreshTokenPayload.put("name", name);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setExpiration(refreshTokenExpirationDate)
                .addClaims(refreshTokenPayload)
                .signWith(Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes()))
                .compact();

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        }catch (JwtException ex) {
            return null;
        }

    }

}
