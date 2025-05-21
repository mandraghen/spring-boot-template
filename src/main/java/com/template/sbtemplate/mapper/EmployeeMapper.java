package com.template.sbtemplate.mapper;

import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.dto.EmployeeDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {AddressMapper.class, DepartmentMapper.class, ProjectMapper.class})
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeDto> {

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
}
