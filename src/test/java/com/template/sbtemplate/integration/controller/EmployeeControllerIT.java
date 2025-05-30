package com.template.sbtemplate.integration.controller;

import com.template.sbtemplate.domain.model.Address;
import com.template.sbtemplate.domain.model.Department;
import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.model.Project;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.dto.AddressDto;
import com.template.sbtemplate.dto.DepartmentDto;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.ProjectDto;
import com.template.sbtemplate.integration.TestContainers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//TODO test put and delete operations
//TODO unit tests with full coverage
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT extends TestContainers {
    private static EmployeeDto EXISTING_EMPLOYEE;
    private static DepartmentDto EXISTING_DEPARTMENT;
    private static AddressDto EXISTING_ADDRESS;
    private static ProjectDto EXISTING_PROJECT;
    private static EmployeeDto NEW_EMPLOYEE;
    private static EmployeeDto NO_EMAIL_EMPLOYEE;
    private static EmployeeDto NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS;
    private static EmployeeDto NEW_COMPLETE_EMPLOYEE_WITH_NEW_RELATIONS;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeAll
    static void setUp() {
        EXISTING_EMPLOYEE = EmployeeDto.builder()
                .id(6L)
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
                .id(6L)
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
    }

    @Test
    void getExistingEmployee_shouldReturnEmployee() {
        ResponseEntity<EmployeeDto> response = restTemplate
                .getForEntity("/employee/" + EXISTING_EMPLOYEE.getId(), EmployeeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EmployeeDto employeeDto = response.getBody();
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(EXISTING_EMPLOYEE.getId());
        assertThat(employeeDto.getName()).isEqualTo(EXISTING_EMPLOYEE.getName());
        assertThat(employeeDto.getPhoneNumber()).isEqualTo(EXISTING_EMPLOYEE.getPhoneNumber());
    }

    @Test
    void getNonExistingEmployee_shouldReturnNotFound() {
        long nonExistingId = 999L;
        ResponseEntity<EmployeeDto> response = restTemplate
                .getForEntity("/employee/" + nonExistingId, EmployeeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void createExistingEmployee_shouldReturnConflict() {
        ResponseEntity<EmployeeDto> response = restTemplate
                .postForEntity("/employee", EXISTING_EMPLOYEE, EmployeeDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        var employeeDto = response.getBody();

        assertThat(employeeDto).isNull();
        assertThat(employeeRepository.existsById(EXISTING_EMPLOYEE.getId())).isTrue();
    }

    @Test
    void saveNewEmployeeWithExistingRelations_shouldPersist() {
        ResponseEntity<EmployeeDto> response = restTemplate
                .postForEntity("/employee", NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS, EmployeeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        EmployeeDto employeeDto = response.getBody();
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isNotNull();

        Employee saved = employeeRepository.findFullById(employeeDto.getId()).orElseThrow();

        assertEmployeeMatches(saved, NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS.toBuilder()
                .department(EXISTING_DEPARTMENT)
                .address(EXISTING_ADDRESS)
                .projects(List.of(EXISTING_PROJECT))
                .build());
    }

    @Test
    void saveNewEmployeeWithNewRelations_shouldPersist() {
        ResponseEntity<EmployeeDto> response = restTemplate
                .postForEntity("/employee", NEW_COMPLETE_EMPLOYEE_WITH_NEW_RELATIONS, EmployeeDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        EmployeeDto employeeDto = response.getBody();
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isNotNull();

        Employee saved = employeeRepository.findFullById(employeeDto.getId()).orElseThrow();

        assertEmployeeMatches(saved, NEW_COMPLETE_EMPLOYEE_WITH_NEW_RELATIONS);
    }

    private void assertEmployeeMatches(Employee actual, EmployeeDto expected) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());

        Department department = actual.getDepartment();
        assertThat(department).isNotNull();
        assertThat(department.getId()).isNotNull();
        if (expected.getDepartment().getId() != null) {
            assertThat(department.getId()).isEqualTo(expected.getDepartment().getId());
        }
        assertThat(department.getCode()).isEqualTo(expected.getDepartment().getCode());
        assertThat(department.getName()).isEqualTo(expected.getDepartment().getName());

        Address address = actual.getAddress();
        assertThat(address).isNotNull();
        assertThat(address.getId()).isNotNull();
        if (expected.getAddress().getId() != null) {
            assertThat(address.getId()).isEqualTo(expected.getAddress().getId());
        }
        assertThat(address.getStreet()).isEqualTo(expected.getAddress().getStreet());
        assertThat(address.getCity()).isEqualTo(expected.getAddress().getCity());
        assertThat(address.getState()).isEqualTo(expected.getAddress().getState());
        assertThat(address.getPostalCode()).isEqualTo(expected.getAddress().getPostalCode());
        assertThat(address.getCountry()).isEqualTo(expected.getAddress().getCountry());

        List<Project> projects = actual.getProjects();
        assertThat(projects).isNotEmpty();

        Project project = projects.getFirst();
        assertThat(project).isNotNull();
        assertThat(project.getId()).isNotNull();
        if (expected.getProjects().getFirst().getId() != null) {
            assertThat(project.getId()).isEqualTo(expected.getProjects().getFirst().getId());
        }
        assertThat(project.getCode()).isEqualTo(expected.getProjects().getFirst().getCode());
        assertThat(project.getName()).isEqualTo(expected.getProjects().getFirst().getName());
    }

    @Test
    void saveEmployee_withNullRelations_shouldPersist() {
        ResponseEntity<EmployeeDto> response = restTemplate
                .postForEntity("/employee", NEW_EMPLOYEE, EmployeeDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        EmployeeDto employeeDto = response.getBody();
        assertThat(employeeDto).isNotNull();

        Optional<Employee> savedOpt = employeeRepository.findFullById(employeeDto.getId());
        assertThat(savedOpt).isPresent();

        Employee saved = savedOpt.get();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDepartment()).isNull();
        assertThat(saved.getAddress()).isNull();
        assertThat(saved.getProjects()).isEmpty();
    }

    @Test
    void createEmployee_withNullEmail_shouldReturnError() {
        ResponseEntity<EmployeeDto> response = restTemplate
                .postForEntity("/employee", NO_EMAIL_EMPLOYEE, EmployeeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
