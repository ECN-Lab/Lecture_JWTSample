package com.sample.jwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sample.jwt.JwtHelper;

@RestController
@RequestMapping("/")
public class HomeController {
	
	
	@RequestMapping(value="/generate", method=RequestMethod.GET)
	public String generate() {
		
		String jwt = JwtHelper.factory()
								.setSubject("JWT Sample")
								.setExpirationDate(null)
								.addClaim("name", "DongHoon")
								.addClaim("city", "Seoul")
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
