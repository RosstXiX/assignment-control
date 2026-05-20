package io.github.rosstxix.assignmentcontrol.infrastructure.error.model;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path
) {
}
