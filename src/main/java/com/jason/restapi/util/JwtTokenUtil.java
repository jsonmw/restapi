package com.jason.restapi.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Utility methods for JWT Auth
 */
@Component
public class JwtTokenUtil {
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Generates new JWT and returns as secure String
     * 
     * @param UserDetails
     * @return jwtToken
     */
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Returns username based on given JWT
     * 
     * @param jwtToken
     * @return Username
     */
    public String getUsernameFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getSubject);
    }

    /**
     * Retrieves claims from given JWT
     * 
     * @param jwtToken
     * @param Function (getter method for desired Claim)
     * @return T of Claim
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    /**
     * Validates user's active token
     * 
     * @param jwtToken
     * @param UserDetails
     * @return Boolean
     */
    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        String email = getUsernameFromToken(jwtToken);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    /**
     * Validates a token's expiration status
     * 
     * @param jwtToken
     * @return Boolean
     */
    private boolean isTokenExpired(String jwtToken) {
        final Date expiration = getClaimFromToken(jwtToken, Claims::getExpiration);
        return expiration.before(new Date());
    }
}
