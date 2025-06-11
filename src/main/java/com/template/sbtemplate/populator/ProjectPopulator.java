package com.template.sbtemplate.populator;

import com.template.sbtemplate.domain.model.Project;
import com.template.sbtemplate.domain.repository.ProjectRepository;
import com.template.sbtemplate.dto.ProjectDto;
import com.template.sbtemplate.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProjectPopulator {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Named("newOrExistingProject")
    public Project populateNewOrExistingProjects(ProjectDto projectDto) {
        return Optional.ofNullable(projectDto)
                .map(dto -> Optional.ofNullable(dto.getId())
                        .flatMap(projectRepository::findById)
                        .orElseGet(() -> Optional.ofNullable(dto.getCode())
                                .flatMap(projectRepository::findByCode)
                                .orElseGet(() -> projectMapper.toNewEntity(dto))))
                .orElse(null);
    }

    @Named("existingProject")
    public Project populateExistingProject(ProjectDto projectDto) {
        return Optional.ofNullable(projectDto)
                .map(dto -> Optional.ofNullable(dto.getId())
                        .flatMap(projectRepository::findById)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Project with id " + dto.getId() + " does not exist.")))
                .orElse(null);
    }
}
