package io.github.rosstxix.assignmentcontrol.feature.employee.repository;

import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @EntityGraph(attributePaths = {"department", "jobTitle", "account"})
    List<Employee> findAll();

    @EntityGraph(attributePaths = {"department", "jobTitle", "account"})
    Optional<Employee> findById(Integer id);

    @EntityGraph(attributePaths = {"department", "jobTitle", "account"})
    List<Employee> findAllByEmploymentStatus(String status);

    @EntityGraph(attributePaths = {"department", "jobTitle", "account"})
    List<Employee> findAllByDepartmentIdAndEmploymentStatus(Integer departmentId, String status);
}
