package io.github.rosstxix.assignmentcontrol.feature.catalog.mapper;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.AssignmentStatus;
import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.Department;
import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.JobTitle;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatalogMapper {
    JobTitleDto toDto(JobTitle jobTitle);
    JobTitle toEntity(JobTitleRequest request);

    AssignmentStatusDto toDto(AssignmentStatus status);
    AssignmentStatus toEntity(AssignmentStatusRequest request);

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerFullName", expression = "java(department.getManager() != null ? department.getManager().getFirstName() + \" \" + department.getManager().getLastName() : null)")
    DepartmentDto toDto(Department department);

    @Mapping(target = "manager", ignore = true)
    Department toEntity(DepartmentRequest request);
}
