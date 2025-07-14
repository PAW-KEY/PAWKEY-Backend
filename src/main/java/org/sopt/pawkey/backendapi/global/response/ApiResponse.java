package org.sopt.pawkey.backendapi.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

	/**
	 * 사용자 및 클라이언트가 참조할 코드
	 */
	private String code;

	/**
	 * 클라이언트에게 노출할 메시지
	 */
	private String message;

	/**
	 * 실제 응답 페이로드
	 */
	private T data;

	public static ApiResponse<Void> success() {
		return success(null);
	}

	/**
	 * 성공 응답 생성
	 *
	 * @param data 반환할 데이터
	 * @param <T>  데이터 타입
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(
			ResponseCode.SUCCESS.getCode(),
			ResponseCode.SUCCESS.getMessage(),
			data
		);
	}

	/**
	 * 오류 응답 생성
	 *
	 * @param errorCode 실패 코드 enum
	 * @param <T>       데이터 타입 (null)
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> error(ResponseCode errorCode) {
		return new ApiResponse<>(
			errorCode.getCode(),
			errorCode.getMessage(),
			null
		);
	}

	/**
	 * BusinessException -> ErrorCode -> ApiResponse 로 매핑될 때 사용
	 *
	 * @param code    커스텀 에러코드
	 * @param message 커스텀 메시지
	 * @param <T>     데이터 타입 (null)
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> error(String code, String message) {
		return new ApiResponse<>(code, message, null);
	}
}
