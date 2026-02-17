package org.sopt.pawkey.backendapi.domain.routes.walk.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum WalkErrorCode implements ErrorCode {

    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "W40401", "산책 세션이 존재하지 않습니다."),
    ALREADY_IN_WALK(HttpStatus.BAD_REQUEST, "W40001", "이미 진행 중인 산책이 있습니다."),
    SESSION_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "W40002", "이미 종료된 산책입니다."),
    INVALID_SESSION_STATUS(HttpStatus.BAD_REQUEST, "W40003", "산책 세션 상태가 올바르지 않습니다."),
    WALK_POINT_SERIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "W50001", "산책 좌표 직렬화에 실패했습니다."),
    WALK_POINT_DESERIALIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "W50002", "산책 좌표 역직렬화에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }


}
