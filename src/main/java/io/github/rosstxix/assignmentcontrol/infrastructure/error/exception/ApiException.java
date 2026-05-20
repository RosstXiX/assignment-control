package io.github.rosstxix.assignmentcontrol.infrastructure.error.exception;

import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ApiErrorCode errorCode;

    protected ApiException(HttpStatus httpStatus, ApiErrorCode errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
