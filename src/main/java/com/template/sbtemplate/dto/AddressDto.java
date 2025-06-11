package com.template.sbtemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AddressDto extends BasicEntityDto {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private EmployeeDto employee;
}
