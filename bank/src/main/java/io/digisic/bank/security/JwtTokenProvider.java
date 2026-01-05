package io.digisic.bank.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.digisic.bank.exception.RestInvalidArguementException;
import io.digisic.bank.model.security.Role;
import io.digisic.bank.service.UserSecurityService;
import io.digisic.bank.util.Constants;
import io.digisic.bank.util.Messages;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    
    private String plainSecretKey = Constants.API_SECRET;
    private SecretKey secretKey;
    
    // Token expiration -> 1 hour (3600000 ms)
    private final long validityInMilliseconds = 3600000;
    
    @Autowired
    private UserSecurityService userSecurityService;
      
    @PostConstruct
    protected void init() {
        // JJWT 0.12.x requires a SecretKey object rather than a plain string.
        // We create a secure HMAC SHA key from your constant secret.
        this.secretKey = Keys.hmacShaKeyFor(plainSecretKey.getBytes(StandardCharsets.UTF_8));
    }
    
    /*
     * Create Token
     */
    public String createToken(String username, List<Role> roles) {
        
        // Convert roles to a simple list of strings for the JWT claim
        List<String> auths = roles.stream()
                                  .map(Role::getName)
                                  .collect(Collectors.toList());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(username)                 // Modern replacement for setSubject
                .claim("auth", auths)              // Set custom claims
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)               // Automatically detects HS256 based on key size
                .compact();
    }
    
    /*
     * Get Authentication
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userSecurityService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    /*
     * Get Username
     */
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)             // Replacement for setSigningKey
                .build()
                .parseSignedClaims(token)          // Replacement for parseClaimsJws
                .getPayload()                      // Replacement for getBody
                .getSubject();
    }
    
    /*
     * Resolve Token 
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(Constants.API_AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(Constants.API_TOKEN_BEGIN)) {
            return bearerToken.substring(Constants.API_TOKEN_BEGIN.length());
        }
        return null;
    }
    
    /*
     * Validate Token  
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RestInvalidArguementException(Messages.API_INVALID_TOKEN);
        }
    }
}