package com.template.sbtemplate.mapper;

import com.template.sbtemplate.domain.model.Department;
import com.template.sbtemplate.dto.DepartmentDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface DepartmentMapper extends GenericScopedMapper<Department, DepartmentDto> {

    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "entityToIdOnlyDto")
    @Mapping(target = "name")
    @Mapping(target = "code")
    @Named("departmentBasic")
    @Override
    DepartmentDto entityToBasicDto(Department department);
}
