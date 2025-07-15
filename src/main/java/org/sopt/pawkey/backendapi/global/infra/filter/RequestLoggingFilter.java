package org.sopt.pawkey.backendapi.global.infra.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@ConditionalOnProperty(
	name = "logging.request.enabled",
	havingValue = "true",
	matchIfMissing = true
)
public class RequestLoggingFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		String uri = httpRequest.getRequestURI();

		// api/v1이 아닌 요청은 로깅하지 않고 바로 통과
		if (!uri.startsWith("/api/v1")) {
			chain.doFilter(request, response);
			return;
		}

		// ContentCachingResponseWrapper는 api/v1 요청에만 적용
		ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(httpResponse);

		String traceId = UUID.randomUUID().toString();
		MDC.put("traceId", traceId);

		long startTime = System.currentTimeMillis();

		try {
			chain.doFilter(request, cachingResponse);
		} finally {
			long duration = System.currentTimeMillis() - startTime;

			// 요청 로그
			logRequest(httpRequest, traceId);

			// 응답 로그
			logResponse(cachingResponse, traceId, duration);

			// 핵심: 캐시된 응답 본문을 실제 응답으로 복사
			cachingResponse.copyBodyToResponse();

			MDC.clear();
		}
	}

	private void logRequest(HttpServletRequest request, String traceId) {
		String method = request.getMethod();
		String uri = request.getRequestURI();

		StringBuilder headers = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);
			headers.append("\n    ").append(name).append(": ").append(value);
		}

		log.info(
			"\n[REQUEST]\n  Method: {}\n  URI: {}\n  Headers:{}\n  TraceId: {}",
			method,
			uri,
			!headers.isEmpty() ? headers.toString() : " (none)",
			traceId
		);
	}

	private void logResponse(ContentCachingResponseWrapper response, String traceId, long duration) {
		String body = "";
		String code = "";
		String message = "";

		try {
			byte[] content = response.getContentAsByteArray();
			if (content.length > 0) {
				body = new String(content, StandardCharsets.UTF_8);
				JsonNode jsonNode = objectMapper.readTree(body);
				code = jsonNode.path("code").asText();
				message = jsonNode.path("message").asText();
			}
		} catch (Exception e) {
			log.debug("Response body parsing failed: {}", e.getMessage());
		}

		log.info(
			"\n[RESPONSE]\n  Status: {}\n  Duration: {} ms\n  Code: {}\n  Message: {}\n  TraceId: {}",
			response.getStatus(),
			duration,
			code,
			message,
			traceId
		);
	}
}