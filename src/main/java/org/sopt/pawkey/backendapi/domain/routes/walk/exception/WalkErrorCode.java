package org.sopt.pawkey.backendapi.domain.routes.walk.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum WalkErrorCode implements ErrorCode {

    ALREADY_IN_WALK(HttpStatus.BAD_REQUEST,"C40009", "이미 진행 중인 산책이 있습니다.");

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
