package com.openclassrooms.api.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
public class SpringSecurityConfig {
	
	// secret word for JWT generation from .env file 	
	@Value("${JWT_SECRET}")
	private String jwtKey;
               
	@Bean
	// method for global requests treating, switching to the stateless session policy 	
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
				
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
            		// all requests except user registration and login should have token
            		.requestMatchers("/api/auth/register", "/api/auth/login").permitAll() 
	        		.anyRequest().authenticated()
        		)
                .httpBasic(Customizer.withDefaults())
                .build();       
    }

    @Bean
    // method for JWT generation    
    JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
	}

    @Bean
    // method for JWT verification
    JwtDecoder jwtDecoder() {
		SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
	}
    
    @Bean
    // method for using password encryption class    
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
