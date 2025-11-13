package com.openclassrooms.api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.model.User;

@Service
public class JWTService {
	private JwtEncoder jwtEncoder;
    
    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    
    public String generateToken(User user) {
    	// get time now
    	Instant now = Instant.now();
        
    	// construct token generation    	
    	JwtClaimsSet claims = JwtClaimsSet.builder()
        		.issuer("self")
        		.issuedAt(now)
        		.expiresAt(now.plus(1, ChronoUnit.DAYS))
        		.subject(user.getEmail() + user.getName())
        		.build();
        
    	// add params for token generation    	
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        
        // return token        
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
