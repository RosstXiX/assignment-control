package io.github.rosstxix.assignmentcontrol.feature.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeUpdateRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String middleName,
        @NotNull Integer departmentId,
        @NotNull Integer jobTitleId
) {
}
