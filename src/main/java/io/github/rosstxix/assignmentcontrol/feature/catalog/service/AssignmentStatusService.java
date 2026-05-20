package io.github.rosstxix.assignmentcontrol.feature.catalog.service;

import io.github.rosstxix.assignmentcontrol.feature.catalog.domain.AssignmentStatus;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.AssignmentStatusDto;
import io.github.rosstxix.assignmentcontrol.feature.catalog.dto.AssignmentStatusRequest;
import io.github.rosstxix.assignmentcontrol.feature.catalog.mapper.CatalogMapper;
import io.github.rosstxix.assignmentcontrol.feature.catalog.repository.AssignmentStatusRepository;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssignmentStatusService {
    private final AssignmentStatusRepository repository;
    private final CatalogMapper mapper;

    public AssignmentStatusService(AssignmentStatusRepository repository, CatalogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AssignmentStatusDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional
    public AssignmentStatusDto save(AssignmentStatusRequest request) {
        AssignmentStatus status = mapper.toEntity(request);
        return mapper.toDto(repository.save(status));
    }

    @Transactional
    public AssignmentStatusDto update(Integer id, AssignmentStatusRequest request) {
        AssignmentStatus existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundApiException("Assignment status not found with id: " + id));
        existing.setName(request.name());
        return mapper.toDto(repository.save(existing));
    }
}
