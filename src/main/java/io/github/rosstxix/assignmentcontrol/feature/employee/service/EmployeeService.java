package io.github.rosstxix.assignmentcontrol.feature.employee.service;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.Department;
import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.JobTitle;
import io.github.rosstxix.assignmentcontrol.feature.catalog.repository.DepartmentRepository;
import io.github.rosstxix.assignmentcontrol.feature.catalog.repository.JobTitleRepository;
import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Account;
import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Employee;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeCreateRequest;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeCreateResponse;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeDto;
import io.github.rosstxix.assignmentcontrol.feature.employee.dto.EmployeeUpdateRequest;
import io.github.rosstxix.assignmentcontrol.feature.employee.mapper.EmployeeMapper;
import io.github.rosstxix.assignmentcontrol.feature.employee.repository.AccountRepository;
import io.github.rosstxix.assignmentcontrol.feature.employee.repository.EmployeeRepository;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.BusinessApiException;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.assignmentcontrol.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {
    private final EmployeeRepository repository;
    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;
    private final JobTitleRepository jobTitleRepository;
    private final EmployeeMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeDto> findAll() {
        if (SecurityUtils.isAdmin()) {
            return repository.findAllByEmploymentStatus("ACTIVE").stream().map(mapper::toDto).toList();
        } else {
            return repository.findAllByDepartmentIdAndEmploymentStatus(SecurityUtils.getCurrentDepartmentId(), "ACTIVE")
                    .stream().map(mapper::toDto).toList();
        }
    }

    @Transactional
    public EmployeeCreateResponse create(EmployeeCreateRequest request) {
        boolean isAdmin = SecurityUtils.isAdmin();
        Integer currentDepartmentId = SecurityUtils.getCurrentDepartmentId();

        if (!isAdmin) {
            if (!request.departmentId().equals(currentDepartmentId)) {
                throw new BusinessApiException("Managers can only create employees in their own department");
            }
            if (!"ROLE_EMPLOYEE".equals(request.role())) {
                throw new BusinessApiException("Managers can only create accounts with ROLE_EMPLOYEE");
            }
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new EntityNotFoundApiException("Department not found"));
        JobTitle jobTitle = jobTitleRepository.findById(request.jobTitleId())
                .orElseThrow(() -> new EntityNotFoundApiException("Job title not found"));

        String requestedRole = request.role().replace("ROLE_", "");

        if ("MANAGER".equals(requestedRole) && department.getManager() != null) {
            throw new BusinessApiException("A manager has already been appointed for this department");
        }

        if ("EMPLOYEE".equals(requestedRole) && department.getManager() == null) {
            throw new BusinessApiException("The first employee created in the department must have the MANAGER role");
        }

        Employee employee = Employee.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .middleName(request.middleName())
                .department(department)
                .jobTitle(jobTitle)
                .employmentStatus("ACTIVE")
                .build();

        employee = repository.save(employee);

        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String role = request.role();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        Account account = Account.builder()
                .username(request.username())
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .employee(employee)
                .build();

        accountRepository.save(account);

        employee.setAccount(account);

        if ("MANAGER".equals(requestedRole)) {
            department.setManager(employee);
            departmentRepository.save(department);
        }

        return new EmployeeCreateResponse(mapper.toDto(employee), account.getUsername(), rawPassword);
    }

    @Transactional
    public EmployeeDto update(Integer id, EmployeeUpdateRequest request) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Employee not found"));

        existing.setFirstName(request.firstName());
        existing.setLastName(request.lastName());
        existing.setMiddleName(request.middleName());

        if (request.departmentId() != null) {
            Department department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new EntityNotFoundApiException("Department not found"));
            existing.setDepartment(department);
        }

        if (request.jobTitleId() != null) {
            JobTitle jobTitle = jobTitleRepository.findById(request.jobTitleId())
                    .orElseThrow(() -> new EntityNotFoundApiException("Job title not found"));
            existing.setJobTitle(jobTitle);
        }

        return mapper.toDto(repository.save(existing));
    }

    @Transactional
    public void fire(Integer id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Employee not found"));

        Department dept = employee.getDepartment();
        if (dept.getManager() != null && dept.getManager().getId().equals(employee.getId())) {
            throw new BusinessApiException("You cannot terminate the current department head. First, appoint another employee to this position using the organizational chart.");
        }

        employee.setEmploymentStatus("FIRED");
        repository.save(employee);
    }
}
