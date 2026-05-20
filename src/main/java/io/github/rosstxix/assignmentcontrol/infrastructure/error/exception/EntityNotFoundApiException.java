package io.github.rosstxix.assignmentcontrol.infrastructure.error.exception;

import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class EntityNotFoundApiException extends ApiException {
    public EntityNotFoundApiException(String message) {
        super(HttpStatus.NOT_FOUND, ApiErrorCode.ENTITY_NOT_FOUND, message);
    }
}
