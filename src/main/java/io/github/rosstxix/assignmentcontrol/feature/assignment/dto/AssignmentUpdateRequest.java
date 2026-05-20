package io.github.rosstxix.assignmentcontrol.feature.assignment.dto;

import java.time.LocalDate;

public record AssignmentUpdateRequest(
        String title,
        String content,
        LocalDate dueDate,
        Integer executorId,
        Integer statusId,
        LocalDate actualCompletionDate
) {
}
