package io.github.rosstxix.assignmentcontrol.feature.assignment.service;

import io.github.rosstxix.assignmentcontrol.feature.assignment.domain.Assignment;
import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentCreateRequest;
import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentDto;
import io.github.rosstxix.assignmentcontrol.feature.assignment.dto.AssignmentUpdateRequest;
import io.github.rosstxix.assignmentcontrol.feature.assignment.mapper.AssignmentMapper;
import io.github.rosstxix.assignmentcontrol.feature.assignment.repository.AssignmentRepository;
import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.AssignmentStatus;
import io.github.rosstxix.assignmentcontrol.feature.catalog.repository.AssignmentStatusRepository;
import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Employee;
import io.github.rosstxix.assignmentcontrol.feature.employee.repository.EmployeeRepository;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.BusinessApiException;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.assignmentcontrol.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {
    private final AssignmentRepository repository;
    private final AssignmentStatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;
    private final AssignmentMapper mapper;

    private static final Set<String> TERMINAL_STATUSES = Set.of("COMPLETED", "CANCELLED");

    public List<AssignmentDto> findAll(LocalDate date, LocalDate from, LocalDate to) {
        List<Assignment> assignments;

        if (SecurityUtils.isAdmin()) {
            assignments = repository.findAllAdmin(date, from, to);
        } else if (SecurityUtils.hasRole("MANAGER")) {
            assignments = repository.findAllByDepartment(SecurityUtils.getCurrentDepartmentId(), date, from, to);
        } else {
            assignments = repository.findAllByExecutor(SecurityUtils.getCurrentEmployeeId(), date, from, to);
        }

        return assignments.stream().map(mapper::toDto).toList();
    }

    public List<AssignmentDto> getDaily() {
        return findAll(LocalDate.now(), null, null);
    }

    public AssignmentDto findById(Integer id) {
        Assignment assignment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Assignment not found"));

        checkAccess(assignment);

        return mapper.toDto(assignment);
    }

    @Transactional
    public AssignmentDto create(AssignmentCreateRequest request) {
        boolean isAdmin = SecurityUtils.isAdmin();
        Integer currentEmployeeId = SecurityUtils.getCurrentEmployeeId();
        Integer currentDepartmentId = SecurityUtils.getCurrentDepartmentId();

        Employee issuer = employeeRepository.findById(currentEmployeeId)
                .orElseThrow(() -> new EntityNotFoundApiException("Issuer not found"));
        Employee executor = employeeRepository.findById(request.executorId())
                .orElseThrow(() -> new EntityNotFoundApiException("Executor not found"));

        if (!"ACTIVE".equals(executor.getEmploymentStatus())) {
            throw new BusinessApiException("Cannot assign to fired employee");
        }

        if (!isAdmin) {
            if (!executor.getDepartment().getId().equals(currentDepartmentId)) {
                throw new BusinessApiException("Managers can only assign to employees in their department");
            }
        }

        AssignmentStatus status = statusRepository.findByName("IN PROGRESS")
                .orElseThrow(() -> new EntityNotFoundApiException("Default status 'IN PROGRESS' not found"));

        Assignment assignment = Assignment.builder()
                .serialNumber(repository.findMaxSerialNumber() + 1)
                .title(request.title())
                .content(request.content())
                .issueDate(request.issueDate())
                .dueDate(request.dueDate())
                .issuer(issuer)
                .executor(executor)
                .status(status)
                .build();

        return mapper.toDto(repository.save(assignment));
    }

    @Transactional
    public AssignmentDto update(Integer id, AssignmentUpdateRequest request) {
        Assignment assignment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Assignment not found"));

        boolean isAdmin = SecurityUtils.isAdmin();
        boolean isManager = SecurityUtils.hasRole("MANAGER");
        boolean isEmployee = SecurityUtils.hasRole("EMPLOYEE");

        if (!isAdmin && TERMINAL_STATUSES.contains(assignment.getStatus().getName())) {
            throw new BusinessApiException("Cannot edit assignment in terminal status");
        }

        if (isEmployee && !isManager && !isAdmin) {
            if (!assignment.getExecutor().getId().equals(SecurityUtils.getCurrentEmployeeId())) {
                throw new AccessDeniedException("Cannot edit not own assignment");
            }
            if (request.statusId() != null) {
                updateStatus(assignment, request.statusId());
            }
            if (request.actualCompletionDate() != null) {
                assignment.setActualCompletionDate(request.actualCompletionDate());
            }
        } else {
            if (isManager && !isAdmin) {
                checkAccess(assignment);
            }

            if (request.title() != null) assignment.setTitle(request.title());
            if (request.content() != null) assignment.setContent(request.content());
            if (request.dueDate() != null) assignment.setDueDate(request.dueDate());

            if (request.executorId() != null) {
                Employee executor = employeeRepository.findById(request.executorId())
                        .orElseThrow(() -> new EntityNotFoundApiException("Executor not found"));
                if (!"ACTIVE".equals(executor.getEmploymentStatus())) {
                    throw new BusinessApiException("Cannot assign to fired employee");
                }
                if (!isAdmin && !executor.getDepartment().getId().equals(SecurityUtils.getCurrentDepartmentId())) {
                    throw new BusinessApiException("Managers can only assign to employees in their department");
                }
                assignment.setExecutor(executor);
            }

            if (request.statusId() != null) {
                updateStatus(assignment, request.statusId());
            }
            if (request.actualCompletionDate() != null) {
                assignment.setActualCompletionDate(request.actualCompletionDate());
            }
        }

        if (assignment.getActualCompletionDate() != null && assignment.getActualCompletionDate().isBefore(assignment.getIssueDate())) {
            throw new BusinessApiException("Actual completion date cannot be before issue date");
        }

        return mapper.toDto(repository.save(assignment));
    }

    private void updateStatus(Assignment assignment, Integer statusId) {
        AssignmentStatus status = statusRepository.findById(statusId)
                .orElseThrow(() -> new EntityNotFoundApiException("Status not found"));
        assignment.setStatus(status);
        if ("COMPLETED".equals(status.getName()) && assignment.getActualCompletionDate() == null) {
            assignment.setActualCompletionDate(LocalDate.now());
        }
    }

    private void checkAccess(Assignment assignment) {
        if (SecurityUtils.isAdmin()) return;

        if (SecurityUtils.hasRole("MANAGER")) {
            Integer currentDepartmentId = SecurityUtils.getCurrentDepartmentId();
            boolean isIssuerInDept = assignment.getIssuer().getDepartment().getId().equals(currentDepartmentId);
            boolean isExecutorInDept = assignment.getExecutor().getDepartment().getId().equals(currentDepartmentId);
            if (!isIssuerInDept && !isExecutorInDept) {
                throw new AccessDeniedException("Access denied to assignment outside department");
            }
        } else {
            if (!assignment.getExecutor().getId().equals(SecurityUtils.getCurrentEmployeeId())) {
                throw new AccessDeniedException("Access denied to not own assignment");
            }
        }
    }
}
