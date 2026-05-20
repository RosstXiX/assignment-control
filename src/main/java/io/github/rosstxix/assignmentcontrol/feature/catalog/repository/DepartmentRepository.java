package io.github.rosstxix.assignmentcontrol.feature.catalog.repository;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("select d from Department d left join fetch d.manager")
    List<Department> findAllWithManager();
}
