package io.github.rosstxix.assignmentcontrol.feature.catalog.service;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.JobTitle;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.JobTitleDto;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.JobTitleRequest;
import io.github.rosstxix.assignmentcontrol.feature.catalog.mapper.CatalogMapper;
import io.github.rosstxix.assignmentcontrol.feature.catalog.repository.JobTitleRepository;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class JobTitleService {
    private final JobTitleRepository repository;
    private final CatalogMapper mapper;

    public JobTitleService(JobTitleRepository repository, CatalogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<JobTitleDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional
    public JobTitleDto save(JobTitleRequest request) {
        JobTitle jobTitle = mapper.toEntity(request);
        return mapper.toDto(repository.save(jobTitle));
    }

    @Transactional
    public JobTitleDto update(Integer id, JobTitleRequest request) {
        JobTitle existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Job title not found with id: " + id));
        existing.setName(request.name());
        return mapper.toDto(repository.save(existing));
    }
}
