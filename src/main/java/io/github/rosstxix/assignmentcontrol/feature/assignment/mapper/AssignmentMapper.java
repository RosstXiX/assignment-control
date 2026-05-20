package io.github.rosstxix.assignmentcontrol.feature.assignment.mapper;

import io.github.rosstxix.assignmentcontrol.feature.assignment.domain.Assignment;
import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    @Mapping(target = "issuerId", source = "issuer.id")
    @Mapping(target = "issuerFullName", expression = "java(assignment.getIssuer().getFirstName() + \" \" + assignment.getIssuer().getLastName())")
    @Mapping(target = "executorId", source = "executor.id")
    @Mapping(target = "executorFullName", expression = "java(assignment.getExecutor().getFirstName() + \" \" + assignment.getExecutor().getLastName())")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "statusName", source = "status.name")
    AssignmentDto toDto(Assignment assignment);
}
