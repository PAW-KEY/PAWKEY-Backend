package org.sopt.pawkey.backendapi.global.exception.handler;

import java.util.Objects;
import java.util.stream.Collectors;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.GlobalErrorCode;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.sopt.pawkey.backendapi.global.response.ResponseCode;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
		var ec = ex.getErrorCode();
		log.warn("BusinessException: code={}, message={}", ec.getCode(), ex.getMessage());

		return ResponseEntity.status(ec.getStatus())
			.body(ApiResponse.error(ec.getCode(), ec.getMessage()));
	}

	/**
	 * DTO 유효성 검증 실패 처리 (@Valid, @Validated)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String messages = ex.getBindingResult().getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining("; "));

		log.warn("Validation failed: {}", messages);

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), messages));
	}

	/**
	 * 요청 본문(JSON 파싱 오류 처리)
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
		log.warn("JSON parse error: {}", ex.getMessage());

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), "잘못된 요청 본문입니다"));
	}

	/**
	 * MissingServletRequestParameterException 등 바인딩 오류 처리
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
		String messages = ex.getBindingResult().getFieldErrors().stream()
			.map(e -> e.getField() + ": " + e.getDefaultMessage())
			.collect(Collectors.joining("; "));
		log.warn("Bind error: {}", messages);

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), messages));
	}

	/**
	 * 필수 multipart 파트 누락 예외를 처리합니다.
	 */
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ApiResponse<Void>> handleMissingPart(MissingServletRequestPartException ex) {

		String partName = ex.getRequestPartName(); // ex.getMessage()는 전체 메시지
		String message = String.format("필수 입력값 '%s'가 누락되었습니다.", partName);

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), message));
	}

	/**
	 * 제약조건 위반 처리(@Validated on path, request param 등)
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
		String messages = ex.getConstraintViolations().stream()
			.map(v -> v.getMessage())
			.collect(Collectors.joining("; "));
		log.warn("Constraint violation: {}", messages);

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), messages));
	}

	/**
	 * 지원하지 않는 HTTP 메서드 처리
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		String msg = String.format("지원하지 않는 메서드입니다. 허용된 메서드: %s",
			String.join(", ", ex.getSupportedHttpMethods().stream()
				.map(HttpMethod::name).toList()));
		log.warn("Method not supported: {}", ex.getMethod());

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), msg));
	}

	/**
	 * 지원하지 않는 미디어 타입 처리
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
		String msg = String.format("지원하지 않는 미디어 타입입니다. 허용된 타입: %s",
			String.join(", ", ex.getSupportedMediaTypes().stream()
				.map(Objects::toString).toList()));
		log.warn("Media type not supported: {}", ex.getContentType());

		return ResponseEntity.status(ResponseCode.BAD_REQUEST.getStatus())
			.body(ApiResponse.error(ResponseCode.BAD_REQUEST.getCode(), msg));
	}

	/**
	 * 핸들러 미발견 처리 (404)
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(NoHandlerFoundException ex) {
		log.warn("No handler found: {}", ex.getRequestURL());

		return ResponseEntity.status(ResponseCode.NOT_FOUND.getStatus())
			.body(ApiResponse.error(ResponseCode.NOT_FOUND.getCode(), "요청 URL을 찾을 수 없습니다"));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ApiResponse<Void>> handleMissingRequestHeader(MissingRequestHeaderException ex) {

		return ResponseEntity.status(GlobalErrorCode.MISSING_REQUIRED_HEADER.getStatus())
			.body(ApiResponse.error(GlobalErrorCode.MISSING_REQUIRED_HEADER.getCode(),
				GlobalErrorCode.MISSING_REQUIRED_HEADER.getMessage() + " (" + ex.getHeaderName() + ")"));
	}

	/**
	 * 그 외 모든 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, WebRequest req) {
		log.error("Unexpected error", ex);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiResponse.error(GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(),
				GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
	}
}
