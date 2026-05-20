package io.github.rosstxix.assignmentcontrol.feature.employee.mapper;

import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Employee;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "jobTitleId", source = "jobTitle.id")
    @Mapping(target = "jobTitleName", source = "jobTitle.name")
    @Mapping(target = "role", source = "account.role")
    EmployeeDto toDto(Employee employee);
}
