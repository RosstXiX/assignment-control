package io.github.rosstxix.assignmentcontrol.infrastructure.error;

import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ErrorResponseFactory {
    public ErrorResponse create(HttpStatus status, ApiErrorCode errorCode, String message, String path) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                errorCode.getValue(),
                message,
                path
        );
    }
}
