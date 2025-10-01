// package org.sopt.pawkey.backendapi.global.config.interceptor;
//
// import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;
//
// import org.sopt.pawkey.backendapi.global.exception.BusinessException;
// import org.sopt.pawkey.backendapi.global.exception.GlobalErrorCode;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerInterceptor;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
//
// @Component
// public class UserHeaderInterceptor implements HandlerInterceptor {
//
// 	@Override
// 	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
// 		String uri = request.getRequestURI();
//
// 		// 로그인/토큰 재발급 같은 공개 엔드포인트는 헤더 검사 스킵
// 		if (uri.startsWith("/api/v1/auth")) {
// 			return true;
// 		}
// 		if (uri.startsWith("/api/v1/posts")) {
// 			return true;
// 		}
//
// 		String userId = request.getHeader(USER_ID_HEADER);
// 		if (userId == null || userId.isBlank()) {
// 			throw new BusinessException(GlobalErrorCode.MISSING_REQUIRED_HEADER);
// 		}
//
// 		// 인터셉터에서 꺼낸 사용자 ID를 다음 로직에서 재사용하기 위해 일시적으로 저장하는 의미
// 		request.setAttribute("USER_ID", userId);
//
// 		return true;
// 	}
// }
//
