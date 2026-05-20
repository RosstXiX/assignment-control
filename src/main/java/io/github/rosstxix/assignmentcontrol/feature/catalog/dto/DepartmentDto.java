package io.github.rosstxix.assignmentcontrol.feature.catalog.dto;

public record DepartmentDto(
        Integer id,
        String name,
        Integer managerId,
        String managerFullName
) {
}
