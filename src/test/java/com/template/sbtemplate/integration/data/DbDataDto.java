package com.template.sbtemplate.integration.data;

import com.template.sbtemplate.dto.AddressDto;
import com.template.sbtemplate.dto.DepartmentDto;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.ProjectDto;

import java.util.List;

public class DbDataDto {
    public static EmployeeDto EXISTING_EMPLOYEE;
    public static DepartmentDto EXISTING_DEPARTMENT;
    public static AddressDto EXISTING_ADDRESS;
    public static ProjectDto EXISTING_PROJECT;
    public static EmployeeDto NEW_EMPLOYEE;
    public static EmployeeDto NO_EMAIL_EMPLOYEE;
    public static EmployeeDto NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS;
    public static EmployeeDto NEW_COMPLETE_EMPLOYEE_WITH_NEW_RELATIONS;
    public static DepartmentDto OTHER_DEPARTMENT;
    public static AddressDto OTHER_ADDRESS;
    public static ProjectDto OTHER_PROJECT;
    public static EmployeeDto NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS2;

    static {
        EXISTING_EMPLOYEE = EmployeeDto.builder()
                .id(1L)
                .name("Salvo")
                .email("salvo@salvo.it")
                .phoneNumber("321321231")
                .build();

        NEW_EMPLOYEE = EmployeeDto.builder()
                .name("John Doe")
                .email("john.doe@doe.com")
                .phoneNumber("123456789")
                .build();

        NO_EMAIL_EMPLOYEE = NEW_EMPLOYEE.toBuilder()
                .email("")
                .build();

        EXISTING_ADDRESS = AddressDto.builder()
                .id(1L)
                .street("via Piave")
                .city("Casorezzo")
                .postalCode("20003")
                .country("Italia")
                .build();

        EXISTING_DEPARTMENT = DepartmentDto.builder()
                .id(1L)
                .code("123hhoih")
                .name("dip")
                .build();

        EXISTING_PROJECT = ProjectDto.builder()
                .id(1L)
                .code("pro123")
                .name("project")
                .build();

        NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS = EmployeeDto.builder()
                .name("Jane Doe")
                .email("jane.doe@doe.com")
                .phoneNumber("987654321")
                .department(DepartmentDto.builder()
                        .id(EXISTING_DEPARTMENT.getId())
                        .build())
                .address(AddressDto.builder()
                        .id(EXISTING_ADDRESS.getId())
                        .build())
                .projects(List.of(ProjectDto.builder()
                        .id(EXISTING_PROJECT.getId())
                        .build()))
                .build();

        NEW_COMPLETE_EMPLOYEE_WITH_NEW_RELATIONS = EmployeeDto.builder()
                .name("Jane Doe2")
                .email("jane.doe2@doe.com")
                .phoneNumber("987654321")
                .department(DepartmentDto.builder()
                        .code("ENG")
                        .name("Engineering")
                        .build())
                .address(AddressDto.builder()
                        .street("123 Main St")
                        .city("Springfield")
                        .state("IL")
                        .postalCode("62701")
                        .country("USA")
                        .build())
                .projects(List.of(ProjectDto.builder()
                        .code("PROJ-123")
                        .name("Project Alpha")
                        .build()))
                .build();

        OTHER_ADDRESS = AddressDto.builder()
                .id(2L)
                .street("via Po")
                .city("'Milano'")
                .postalCode("20100")
                .country("Italia")
                .build();

        OTHER_DEPARTMENT = DepartmentDto.builder()
                .id(2L)
                .code("1234")
                .name("New Department")
                .build();

        OTHER_PROJECT = ProjectDto.builder()
                .id(2L)
                .code("1234")
                .name("New Project")
                .build();

        NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS2 = EmployeeDto.builder()
                .id(99L)
                .name("Jim Doe")
                .email("jim.doe@doe.com")
                .phoneNumber("654433222")
                .department(DepartmentDto.builder()
                        .id(EXISTING_DEPARTMENT.getId())
                        .build())
                .address(AddressDto.builder()
                        .id(EXISTING_ADDRESS.getId())
                        .build())
                .projects(List.of(ProjectDto.builder()
                        .id(EXISTING_PROJECT.getId())
                        .build()))
                .build();
    }
}
