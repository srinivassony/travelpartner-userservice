package com.travelpartner.user_service.utill;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	   private final String jwtSecret = "Travelpartner";
	    private final long jwtExpirationMs = 86400000;  // 24 hours

	    public String generateToken(Authentication authentication) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        return Jwts.builder()
	            .setSubject(userDetails.getUsername())
	            .setIssuedAt(new Date())
	            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
	            .signWith(SignatureAlgorithm.HS512, jwtSecret)
	            .compact();
	    }

	    public boolean validateToken(String token) {
	        try {
	            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    public String getEmailFromToken(String token) {
	        return Jwts.parser()
	            .setSigningKey(jwtSecret)
	            .parseClaimsJws(token)
	            .getBody()
	            .getSubject();
	    }
}
