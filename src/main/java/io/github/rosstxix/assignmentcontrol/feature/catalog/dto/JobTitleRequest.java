package io.github.rosstxix.assignmentcontrol.feature.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record JobTitleRequest(
        @NotBlank String name
) {
}
