package io.github.rosstxix.assignmentcontrol.feature.assignment.controller;

import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentCreateRequest;
import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentDto;
import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentUpdateRequest;
import io.github.rosstxix.assignmentcontrol.feature.assignment.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping
    public List<AssignmentDto> getAssignments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return assignmentService.findAll(date, from, to);
    }

    @GetMapping("/daily")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<AssignmentDto> getDailyAssignments() {
        return assignmentService.getDaily();
    }

    @GetMapping("/{id}")
    public AssignmentDto getAssignment(@PathVariable Integer id) {
        return assignmentService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public AssignmentDto createAssignment(@RequestBody @Valid AssignmentCreateRequest request) {
        return assignmentService.create(request);
    }

    @PutMapping("/{id}")
    public AssignmentDto updateAssignment(@PathVariable Integer id, @RequestBody @Valid AssignmentUpdateRequest request) {
        return assignmentService.update(id, request);
    }
}
