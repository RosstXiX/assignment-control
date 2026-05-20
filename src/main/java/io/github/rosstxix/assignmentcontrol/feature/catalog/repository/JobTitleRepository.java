package io.github.rosstxix.assignmentcontrol.feature.catalog.repository;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTitleRepository extends JpaRepository<JobTitle, Integer> {
}
