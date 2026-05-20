package io.github.rosstxix.assignmentcontrol.feature.catalog.service;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.Department;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.DepartmentDto;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.DepartmentRequest;
import io.github.rosstxix.assignmentcontrol.feature.catalog.mapper.CatalogMapper;
import io.github.rosstxix.assignmentcontrol.feature.catalog.repository.DepartmentRepository;
import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Account;
import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Employee;
import io.github.rosstxix.assignmentcontrol.feature.employee.repository.AccountRepository;
import io.github.rosstxix.assignmentcontrol.feature.employee.repository.EmployeeRepository;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.BusinessApiException;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentService {
    private final DepartmentRepository repository;
    private final EmployeeRepository employeeRepository;
    private final CatalogMapper mapper;
    private final AccountRepository accountRepository;

    public DepartmentService(DepartmentRepository repository, EmployeeRepository employeeRepository, CatalogMapper mapper, AccountRepository accountRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.accountRepository = accountRepository;
    }

    public List<DepartmentDto> findAll() {
        return repository.findAllWithManager().stream().map(mapper::toDto).toList();
    }

    @Transactional
    public DepartmentDto save(DepartmentRequest request) {
        Department department = repository.save(mapper.toEntity(request));
        if (request.managerId() != null) {
            Employee manager = employeeRepository.findById(request.managerId())
                    .orElseThrow(() -> new EntityNotFoundApiException("Employee not found with id: " + request.managerId()));
            
            if (!manager.getDepartment().getId().equals(department.getId())) {
                throw new BusinessApiException("Manager must belong to the department");
            }
            department.setManager(manager);
            department = repository.save(department);
        }
        return mapper.toDto(department);
    }

    @Transactional
    public DepartmentDto update(Integer id, DepartmentRequest request) {
        Department existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Department not found with id: " + id));
        existing.setName(request.name());

        Integer oldManagerId = existing.getManager() != null ? existing.getManager().getId() : null;
        Integer newManagerId = request.managerId();

        if (newManagerId != null && !newManagerId.equals(oldManagerId)) {
            Employee newManager = employeeRepository.findById(newManagerId)
                    .orElseThrow(() -> new EntityNotFoundApiException("Employee not found"));

            if (!newManager.getDepartment().getId().equals(existing.getId())) {
                throw new BusinessApiException("Manager must belong to the department");
            }

            if (oldManagerId != null) {
                Account oldAccount = accountRepository.findByEmployeeId(oldManagerId)
                        .orElseThrow(() -> new EntityNotFoundApiException("Account not found for old manager"));

                if (!"ROLE_ADMIN".equals(oldAccount.getRole())) {
                    oldAccount.setRole("ROLE_EMPLOYEE");
                    accountRepository.save(oldAccount);
                }
            }

            Account newAccount = accountRepository.findByEmployeeId(newManagerId)
                    .orElseThrow(() -> new EntityNotFoundApiException("Account not found for new manager"));

            if (!"ROLE_ADMIN".equals(newAccount.getRole())) {
                newAccount.setRole("ROLE_MANAGER");
                accountRepository.save(newAccount);
            }

            existing.setManager(newManager);

        } else if (newManagerId == null && oldManagerId != null) {
            Account oldAccount = accountRepository.findByEmployeeId(oldManagerId)
                    .orElseThrow(() -> new EntityNotFoundApiException("Account not found for old manager"));

            if (!"ROLE_ADMIN".equals(oldAccount.getRole())) {
                oldAccount.setRole("ROLE_EMPLOYEE");
                accountRepository.save(oldAccount);
            }
            existing.setManager(null);
        }

        return mapper.toDto(repository.save(existing));
    }
}
