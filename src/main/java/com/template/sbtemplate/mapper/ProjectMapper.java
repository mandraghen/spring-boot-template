package com.template.sbtemplate.mapper;

import com.template.sbtemplate.domain.model.Project;
import com.template.sbtemplate.dto.ProjectDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ProjectMapper extends GenericScopedMapper<Project, ProjectDto> {

    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "entityToIdOnlyDto")
    @Mapping(target = "name")
    @Mapping(target = "code")
    @Named("projectBasic")
    @Override
    ProjectDto entityToBasicDto(Project project);
}
