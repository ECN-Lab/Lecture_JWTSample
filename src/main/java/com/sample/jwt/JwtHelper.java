package com.sample.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import lombok.Getter;

@Component
public class JwtHelper {
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//  static members
	private static JwtProperty jwtProperty;
	
	@Autowired(required=true) 
	public void setSecurity(JwtProperty security) {
		JwtHelper.jwtProperty = security;
	}
	
	
	
	
	
	public static JwtHelper factory() {
		return new JwtHelper();
	}
	
	
	public static Boolean validation(String jwt) {
    	try {
    		Jwts.parser()
				.setSigningKey(jwtProperty.getSigningKey())
				.parseClaimsJws(jwt);
    		
    		return true;
    	}
    	catch (SignatureException exception) {
    		
    		System.out.println("SignatureException");
    		return false;
        }
    	catch (ExpiredJwtException exception) {
    		
    		System.out.println("ExpiredJwtException");
    		return false;
        }
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    		return false;
    	}
    }
    
    
    
    

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//  members
    private HashMap<String, Object> headerClaims = new HashMap<String, Object>();
    private HashMap<String, Object> payloadClaims = new HashMap<String, Object>();
    @Getter private JwtBuilder jwtBuilder = Jwts.builder();
    
    
    
    
    
    public JwtHelper() {
    	jwtBuilder.setIssuedAt(new Date());
    	
    	addHeaderClaim("typ", "JWT");
    }
    
    
    public JwtHelper addHeaderClaim(String key, Object value) {
    	headerClaims.put(key, value);
    	return this;
    }
    
    
    public JwtHelper addClaim(String key, Object value) {
    	payloadClaims.put(key, value);
    	return this;
    }
    
    
    public JwtHelper setAudience(String aud) {
    	jwtBuilder.setAudience(aud);
    	return this;
    }
    
    
    public JwtHelper setSubject(String sub) {
    	jwtBuilder.setSubject(sub);
    	return this;
    }
    
    
    public JwtHelper setExpirationDate(Long expiration) {
    	Date exp;
    	if (expiration == null)
    		exp = new Date(System.currentTimeMillis() + (jwtProperty.getExpirationTime() * 1000));
    	else
    		exp = new Date(expiration);
        
        
        jwtBuilder.setExpiration(exp);
    	return this;
    }
    
    
    public Claims getClaims(String token) throws IOException, ServletException {
    	
        Claims claims = Jwts.parser()
        					.setSigningKey(jwtProperty.getSigningKey())
        					.parseClaimsJws(token)
        					.getBody();
        
        return claims;
    }
    
    
    public String build() {
    	//  Add claims to header
        for (Entry<String, Object> entry : headerClaims.entrySet()) {
        	jwtBuilder.setHeaderParam(entry.getKey(), entry.getValue());
        }
        
        
    	//  Add claims to payload
        for (Entry<String, Object> entry : payloadClaims.entrySet()) {
        	jwtBuilder.claim(entry.getKey(), entry.getValue());
        }
        
        String jwt = jwtBuilder.signWith(SignatureAlgorithm.HS512, jwtProperty.getSigningKey())
        						.compact();
    	
    	return jwt;
    }
}
