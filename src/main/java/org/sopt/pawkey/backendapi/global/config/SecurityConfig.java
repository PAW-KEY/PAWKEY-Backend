package org.sopt.pawkey.backendapi.global.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	//HTTP 보안 설정: 세션 비활성화 & JWT 인증 설정
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception{
		return http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**", "/health").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder)))
			.build();
	}

	//JWT 디코더 설정: (application-oauth.yml)secret 값으로 HS256 서명 검증
	@Bean
	JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
		// 대칭키 (HS256) 생성
		SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"); //대칭키 (HS256) 생성

		OAuth2TokenValidator<org.springframework.security.oauth2.jwt.Jwt> validator =
			new DelegatingOAuth2TokenValidator<>(
				new JwtTimestampValidator(Duration.ofSeconds(60)) // 시계 오차 60초 허용
			);
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(key)
			.build();

		jwtDecoder.setJwtValidator(validator);

		return jwtDecoder;
	}
}
