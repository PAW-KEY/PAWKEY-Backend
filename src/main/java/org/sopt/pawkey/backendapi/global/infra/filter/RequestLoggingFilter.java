package org.sopt.pawkey.backendapi.global.infra.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Component
@ConditionalOnProperty(
	name = "logging.request.enabled",
	havingValue = "true",
	matchIfMissing = true
)
public class RequestLoggingFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// traceId 생성
		String traceId = UUID.randomUUID().toString();
		MDC.put("traceId", traceId);

		long startTime = System.currentTimeMillis();

		try {
			chain.doFilter(request, response);
		} finally {
			long duration = System.currentTimeMillis() - startTime;

			// 요청 로그
			logRequest(httpRequest, traceId);

			// 응답 로그
			log.info(
				"\n[RESPONSE]\n  Status: {}\n  Duration: {} ms\n  TraceId: {}",
				httpResponse.getStatus(),
				duration,
				traceId
			);

			MDC.clear(); // 꼭 지워주기
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
			"\n[REQUEST]\n  Method: {}\n  URI: {}\n  Headers:{}\n traceId:{}",
			method,
			uri,
			!headers.isEmpty() ? headers.toString() : " (none)",
			traceId
		);
	}
}
