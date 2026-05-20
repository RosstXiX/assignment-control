package io.github.rosstxix.assignmentcontrol.feature.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(
        @NotBlank String name,
        Integer managerId
) {
}
