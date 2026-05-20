package io.github.rosstxix.assignmentcontrol.infrastructure.error.exception;

import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class BusinessApiException extends ApiException {
    public BusinessApiException(String message) {
        super(HttpStatus.BAD_REQUEST, ApiErrorCode.BUSINESS_ERROR, message);
    }
}
