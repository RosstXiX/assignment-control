package io.github.rosstxix.assignmentcontrol.feature.assignment.repository;

import io.github.rosstxix.assignmentcontrol.feature.assignment.domain.Assignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query("SELECT COALESCE(MAX(a.serialNumber), 0) FROM Assignment a")
    Integer findMaxSerialNumber();

    @EntityGraph(attributePaths = {"issuer", "executor", "status"})
    Optional<Assignment> findById(Integer id);

    @EntityGraph(attributePaths = {"issuer", "executor", "status"})
    @Query("""
        SELECT a FROM Assignment a
        WHERE a.dueDate = COALESCE(:date, a.dueDate)
        AND a.issueDate >= COALESCE(:from, a.issueDate)
        AND a.issueDate <= COALESCE(:to, a.issueDate)
        ORDER BY a.serialNumber
    """)
    List<Assignment> findAllAdmin(
            @Param("date") LocalDate date,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @EntityGraph(attributePaths = {"issuer", "executor", "status"})
    @Query("""
        SELECT a FROM Assignment a
        WHERE (a.issuer.department.id = :deptId OR a.executor.department.id = :deptId)
        AND a.dueDate = COALESCE(:date, a.dueDate)
        AND a.issueDate >= COALESCE(:from, a.issueDate)
        AND a.issueDate <= COALESCE(:to, a.issueDate)
        ORDER BY a.serialNumber
    """)
    List<Assignment> findAllByDepartment(
            @Param("deptId") Integer deptId,
            @Param("date") LocalDate date,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @EntityGraph(attributePaths = {"issuer", "executor", "status"})
    @Query("""
        SELECT a FROM Assignment a
        WHERE a.executor.id = :executorId
        AND a.dueDate = COALESCE(:date, a.dueDate)
        AND a.issueDate >= COALESCE(:from, a.issueDate)
        AND a.issueDate <= COALESCE(:to, a.issueDate)
        ORDER BY a.serialNumber
    """)
    List<Assignment> findAllByExecutor(
            @Param("executorId") Integer executorId,
            @Param("date") LocalDate date,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
