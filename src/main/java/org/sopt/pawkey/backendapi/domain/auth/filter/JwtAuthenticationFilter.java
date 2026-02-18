package org.sopt.pawkey.backendapi.domain.auth.filter;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sopt.pawkey.backendapi.domain.auth.application.service.token.TokenService;
import org.sopt.pawkey.backendapi.domain.auth.constants.AuthConstant;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {

		String token = resolveToken(request);

		if (token != null) {

			try{
				// 1. 토큰 유효성 검증 & userId 추출
				Long userId = tokenService.extractUserId(token);
				// 2. 인증 객체 생성 후 SecurityContext에 저장
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userId, null, null);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);

			}

			catch(AuthBusinessException e){
				ErrorCode errorCode = e.getErrorCode();
				response.setStatus(errorCode.getStatus().value());
				response.setContentType("application/json;charset=UTF-8");

				objectMapper.writeValue(response.getWriter(), Map.of(
						"code", errorCode.getCode(),
						"message", errorCode.getMessage()
				));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);
		if (bearerToken != null && bearerToken.startsWith(AuthConstant.BEARER_TOKEN_PREFIX)) {
			return bearerToken.substring(AuthConstant.BEARER_TOKEN_PREFIX.length());
		}
		return null;
	}

	//인증 제외할 엔드포인트
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/api/v1/auth/google/login")
			|| path.startsWith("/api/v1/auth/kakao/login")
			|| path.startsWith("/api/v1/auth/apple/login")
			|| path.startsWith("/api/v1/auth/refresh")
			|| path.startsWith("/swagger-ui")
			|| path.startsWith("/v3/api-docs");
	}
}
