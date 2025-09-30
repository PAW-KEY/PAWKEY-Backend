package org.sopt.pawkey.backendapi.global.auth.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final StringRedisTemplate redisTemplate;

	@Value("${security.jwt.secret}") private String secret;
	@Value("${security.jwt.issuer}") private String issuer;
	@Value("${security.jwt.access-exp-minutes}") private long accessExpMinutes;
	@Value("${security.jwt.refresh-exp-days}") private long refreshExpDays;

	private byte[] keyBytes;
	private static final String REFRESH_PREFIX = "refresh:";



}
