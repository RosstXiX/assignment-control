package io.github.rosstxix.assignmentcontrol.feature.employee.dto;

public record EmployeeDto(
        Integer id,
        String firstName,
        String lastName,
        String middleName,
        Integer departmentId,
        String departmentName,
        Integer jobTitleId,
        String jobTitleName,
        String employmentStatus,
        String role
) {
}
