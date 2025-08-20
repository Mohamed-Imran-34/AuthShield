package com.security.AuthShield.jwtservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class jwtUtils {

    String KEY="dnfgbhnuhfiguehrufgkjnkfdsjhgyujkyuktyuiuhg";

    public String generateToken(String username){

        Map<String,Object> claims =  new HashMap<>();
        return  Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10* 24*60*60 * 1000))
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes()),Jwts.SIG.HS256)
                .compact();

    }

    public Claims extractAllClaims(String token){
        return   Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T>T extractClaim(String token, Function<Claims,T> claimsResovler){
        Claims claims = extractAllClaims(token);
        return claimsResovler.apply(claims);
    }

    public String getUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date  getExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public Boolean validateToken(String Token,String username){
        return (username.equals(getUsername(Token)) && ! getExpiration(Token).before(new Date(System.currentTimeMillis())));
    }
}
