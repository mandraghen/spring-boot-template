package com.template.sbtemplate.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class EmployeeDto extends BasicEntityDto {
    private String name;
    private String email;
    private String phoneNumber;
    private AddressDto address;
    private DepartmentDto department;
    private List<ProjectDto> projects;

    private Long addressId;
    private Long departmentId;
}
