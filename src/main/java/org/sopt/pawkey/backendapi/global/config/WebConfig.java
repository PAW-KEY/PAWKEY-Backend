package org.sopt.pawkey.backendapi.global.config;

import org.sopt.pawkey.backendapi.global.config.interceptor.UserHeaderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final UserHeaderInterceptor userHeaderInterceptor;

	public WebConfig(UserHeaderInterceptor userHeaderInterceptor) {
		this.userHeaderInterceptor = userHeaderInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userHeaderInterceptor)
			.addPathPatterns("/api/**")
			.excludePathPatterns("/api/v1/regions")
		; // 적용하고 싶은 URL 경로
	}
}
