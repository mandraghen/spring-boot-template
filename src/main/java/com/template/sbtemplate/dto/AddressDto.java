package com.template.sbtemplate.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AddressDto extends BasicEntityDto {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private EmployeeDto employee;
}
