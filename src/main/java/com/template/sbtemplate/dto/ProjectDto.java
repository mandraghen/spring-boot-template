package com.template.sbtemplate.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ProjectDto extends BasicEntityDto {
    private String name;
    private String code;
    private List<EmployeeDto> employees;
}
