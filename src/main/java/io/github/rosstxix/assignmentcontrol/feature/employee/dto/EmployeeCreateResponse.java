package io.github.rosstxix.assignmentcontrol.feature.employee.dto;

public record EmployeeCreateResponse(
        EmployeeDto employee,
        String username,
        String password
) {
}
