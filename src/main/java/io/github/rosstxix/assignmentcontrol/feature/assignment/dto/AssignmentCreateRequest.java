package io.github.rosstxix.assignmentcontrol.feature.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AssignmentCreateRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull LocalDate issueDate,
        @NotNull LocalDate dueDate,
        @NotNull Integer executorId
) {
}
