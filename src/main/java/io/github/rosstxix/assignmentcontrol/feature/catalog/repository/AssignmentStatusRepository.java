package io.github.rosstxix.assignmentcontrol.feature.catalog.repository;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentStatusRepository extends JpaRepository<AssignmentStatus, Integer> {
    Optional<AssignmentStatus> findByName(String name);
}
