package io.github.rosstxix.assignmentcontrol.feature.employee.repository;

import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @EntityGraph(attributePaths = {"employee", "employee.department"})
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmployeeId(Integer employeeId);
}
