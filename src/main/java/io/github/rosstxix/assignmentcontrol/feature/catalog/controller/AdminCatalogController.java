package io.github.rosstxix.assignmentcontrol.feature.catalog.controller;

import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.*;
import io.github.rosstxix.assignmentcontrol.feature.catalog.service.AssignmentStatusService;
import io.github.rosstxix.assignmentcontrol.feature.catalog.service.DepartmentService;
import io.github.rosstxix.assignmentcontrol.feature.catalog.service.JobTitleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCatalogController {
    private final JobTitleService jobTitleService;
    private final DepartmentService departmentService;
    private final AssignmentStatusService assignmentStatusService;

    @GetMapping("/job-titles")
    public List<JobTitleDto> getJobTitles() {
        return jobTitleService.findAll();
    }

    @PostMapping("/job-titles")
    public JobTitleDto createJobTitle(@RequestBody @Valid JobTitleRequest request) {
        return jobTitleService.save(request);
    }

    @PutMapping("/job-titles/{id}")
    public JobTitleDto updateJobTitle(@PathVariable Integer id, @RequestBody @Valid JobTitleRequest request) {
        return jobTitleService.update(id, request);
    }

    @GetMapping("/departments")
    public List<DepartmentDto> getDepartments() {
        return departmentService.findAll();
    }

    @PostMapping("/departments")
    public DepartmentDto createDepartment(@RequestBody @Valid DepartmentRequest request) {
        return departmentService.save(request);
    }

    @PutMapping("/departments/{id}")
    public DepartmentDto updateDepartment(@PathVariable Integer id, @RequestBody @Valid DepartmentRequest request) {
        return departmentService.update(id, request);
    }

    @GetMapping("/assignment-statuses")
    public List<AssignmentStatusDto> getAssignmentStatuses() {
        return assignmentStatusService.findAll();
    }

    @PostMapping("/assignment-statuses")
    public AssignmentStatusDto createAssignmentStatus(@RequestBody @Valid AssignmentStatusRequest request) {
        return assignmentStatusService.save(request);
    }

    @PutMapping("/assignment-statuses/{id}")
    public AssignmentStatusDto updateAssignmentStatus(@PathVariable Integer id, @RequestBody @Valid AssignmentStatusRequest request) {
        return assignmentStatusService.update(id, request);
    }
}
