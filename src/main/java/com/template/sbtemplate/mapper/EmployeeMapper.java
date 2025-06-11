package com.template.sbtemplate.mapper;

import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.populator.AddressPopulator;
import com.template.sbtemplate.populator.DepartmentPopulator;
import com.template.sbtemplate.populator.ProjectPopulator;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {AddressMapper.class, AddressPopulator.class, DepartmentMapper.class, DepartmentPopulator.class,
        ProjectMapper.class, ProjectPopulator.class})
public interface EmployeeMapper extends GenericScopedMapper<Employee, EmployeeDto> {

    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "entityToIdOnlyDto")
    @Mapping(target = "name")
    @Mapping(target = "email")
    @Mapping(target = "phoneNumber")
    @Mapping(target = "addressId")
    @Mapping(target = "departmentId")
    @Named("employeeBasic")
    @Override
    EmployeeDto entityToBasicDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "address", qualifiedByName = "newOrExistingAddress")
    @Mapping(target = "department", qualifiedByName = "newOrExistingDepartment")
    @Mapping(target = "projects", qualifiedByName = "newOrExistingProject")
    @Named("employeeNewEntity")
    Employee toNewEntity(EmployeeDto dto);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "address", qualifiedByName = "existingAddress")
    @Mapping(target = "department", qualifiedByName = "existingDepartment")
    @Mapping(target = "projects", qualifiedByName = "existingProject")
    @Named("employeeUpdateEntity")
    Employee toUpdateEntity(EmployeeDto dto);
}
