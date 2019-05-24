package com.sample.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sample.jwt.JwtHelper;
import com.sample.jwt.JwtProperty;

@RestController
@RequestMapping("/")
public class HomeController {
	@Autowired JwtProperty jwtProperty;
	
	
	
	
	
	@RequestMapping(value="/generate", method=RequestMethod.GET)
	public String generate() {
		
		String jwt = JwtHelper.create()
								.setSubject("JWT Sample")
								.setExpiration(System.currentTimeMillis() + (jwtProperty.getExpirationTime() * 1000))
								.addClaim("name", "DongHoon")
								.addClaim("city", "Seoul")
								.addClaim("company", "NTSphere")
								.build();
		return jwt;
	}
	
	
	@RequestMapping(value="/method1", method=RequestMethod.GET)
	public String method1() {
		return "method1";
	}
	
	
	@RequestMapping(value="/method2", method=RequestMethod.GET)
	public String method2() {
		return "method2";
	}
}
