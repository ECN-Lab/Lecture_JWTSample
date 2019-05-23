package com.sample.jwt;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtFilter extends GenericFilterBean {
	private static JwtProperty jwtProperties;
	
	@Autowired(required=true) 
	public void setSecurity(JwtProperty prop) {
		jwtProperties = prop;
	}
    
    
    
    
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
    	
		
        //  URI Filtering
        String requestUri = httpServletRequest.getRequestURI().toLowerCase();
        if (isTokenRequiresUri(requestUri) == true) {
           
    		//  Get JWT from header
    		String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader == null || authHeader.startsWith("Bearer ") == false) {
            	httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            	return;
            }
        	String jwt = authHeader.substring(7);

        	
        	//  Validation JWT
        	if (JwtHelper.validation(jwt) == false) {
            	httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            	return;
        	}
        }
        
    	chain.doFilter(request, response);
	}
	
	
	private Boolean isTokenRequiresUri(String requestUri) {

        Boolean checkToken = false;
        
        
        requestUri = requestUri.toLowerCase();
        
        //  Determine whether the requested Uri is a Uri that should resolve the Token.
        for (String pattern : jwtProperties.getTokenRequireUri()) {
        	if (isMatchUriRegEx(pattern.toLowerCase(), requestUri) == true) {
        		checkToken = true;
        		break;
        	}
        }
        
        
        //  Determine if the requested Uri is a Uri that ignores the Token check.
        for (String pattern : jwtProperties.getTokenIgnoreUri()) {
        	if (isMatchUriRegEx(pattern.toLowerCase(), requestUri) == true) {
        		checkToken = false;
        		break;
        	}
        }
        
        return checkToken;
	}
	
	
	private Boolean isMatchUriRegEx(String pattern, String uri) {
    	pattern = "^" + pattern;								//  RegEx starting character
    	pattern = pattern.replaceAll("[*][*]", ".+");			//  Replace ** to .+ (one or more)
    	pattern = pattern.replaceAll("[*]", ".*");				//  Replace * to .* (zero or more)
    	pattern = pattern.replaceAll("\\/", "\\\\/");			//  Replace / to \/

		return Pattern.matches(pattern, uri);
	}
}