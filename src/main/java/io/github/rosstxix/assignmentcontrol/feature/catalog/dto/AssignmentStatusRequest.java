package io.github.rosstxix.assignmentcontrol.feature.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignmentStatusRequest(
        @NotBlank String name
) {
}
