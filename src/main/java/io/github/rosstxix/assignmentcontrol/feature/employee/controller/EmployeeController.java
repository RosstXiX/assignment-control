package io.github.rosstxix.assignmentcontrol.feature.employee.controller;

import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeCreateRequest;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeCreateResponse;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeDto;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeUpdateRequest;
import io.github.rosstxix.assignmentcontrol.feature.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<EmployeeDto> getEmployees() {
        return employeeService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public EmployeeCreateResponse createEmployee(@RequestBody @Valid EmployeeCreateRequest request) {
        return employeeService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeDto updateEmployee(@PathVariable Integer id, @RequestBody @Valid EmployeeUpdateRequest request) {
        return employeeService.update(id, request);
    }

    @PatchMapping("/{id}/fire")
    @PreAuthorize("hasRole('ADMIN')")
    public void fireEmployee(@PathVariable Integer id) {
        employeeService.fire(id);
    }
}
