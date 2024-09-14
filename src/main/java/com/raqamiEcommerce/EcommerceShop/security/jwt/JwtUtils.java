package com.raqamiEcommerce.EcommerceShop.security.jwt;

import com.raqamiEcommerce.EcommerceShop.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
@Component
public class JwtUtils {
    private String secretkey = "";
    public JwtUtils() {

        try {
            KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk=keygen.generateKey();
            secretkey= Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;
    public String generateToken(Authentication authentication) {
        ShopUserDetails userPrincipal=(ShopUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .subject(userPrincipal.getEmail())
                .claim("id",userPrincipal.getId())
                .claim("role",roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+expirationTime))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey(){

        byte[] keyB= Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyB);
    }
    public String gerUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | io.jsonwebtoken.security.SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
