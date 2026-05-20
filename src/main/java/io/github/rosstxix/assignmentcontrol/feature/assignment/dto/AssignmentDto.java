package io.github.rosstxix.assignmentcontrol.feature.assignment.dto;

import java.time.Instant;
import java.time.LocalDate;

public record AssignmentDto(
        Integer id,
        Integer serialNumber,
        String title,
        String content,
        LocalDate issueDate,
        LocalDate dueDate,
        LocalDate actualCompletionDate,
        Instant createdAt,
        Integer issuerId,
        String issuerFullName,
        Integer executorId,
        String executorFullName,
        Integer statusId,
        String statusName
) {
}
