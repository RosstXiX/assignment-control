package io.github.rosstxix.assignmentcontrol.feature.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeCreateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String middleName,
        @NotNull Integer departmentId,
        @NotNull Integer jobTitleId,
        @NotBlank String username,
        @NotBlank String role
) {
}
