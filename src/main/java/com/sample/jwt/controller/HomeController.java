package com.sample.jwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
	
	
	@RequestMapping(value="/method1", method=RequestMethod.GET)
	public String method1() {
		return "method1";
	}
	
	
	@RequestMapping(value="/method2", method=RequestMethod.GET)
	public String method2() {
		return "method2";
	}
}
