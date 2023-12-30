package com.springboot.blog.security;

import com.springboot.blog.exception.BlogApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;



@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    //generate token
    SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate =new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);


        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key)
                .compact();
        return token;
    }


    //get user from the token
    public String getUsernameFromJWT(String token){

            Claims claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

                    return claims.getSubject();
        }




    // validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload().getSubject();

            return true;
        }catch(MissingClaimException mce) {
            // the parsed JWT did not have the sub claim
            mce.printStackTrace();
        } catch(IncorrectClaimException ice) {
            // the parsed JWT had a sub claim, but its value was not equal to 'jsmith'
            ice.printStackTrace();
        }
        return false;
    }



}
