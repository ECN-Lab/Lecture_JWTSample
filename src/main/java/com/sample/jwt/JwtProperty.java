package com.sample.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="jwt")
@Data
public class JwtProperty {
	private String signingKey;
	private Integer expirationTime;
	private String[] tokenRequireUri;
	private String[] tokenIgnoreUri;
}
