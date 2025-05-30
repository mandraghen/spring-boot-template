package com.template.sbtemplate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class EmployeeDto extends BasicEntityDto {
    private String name;
    @NotBlank
    private String email;
    private String phoneNumber;
    private AddressDto address;
    private DepartmentDto department;
    private List<ProjectDto> projects;

    private Long addressId;
    private Long departmentId;
}
