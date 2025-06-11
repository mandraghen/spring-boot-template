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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static com.template.sbtemplate.integration.data.DbDataDto.EXISTING_ADDRESS;
import static com.template.sbtemplate.integration.data.DbDataDto.EXISTING_DEPARTMENT;
import static com.template.sbtemplate.integration.data.DbDataDto.EXISTING_EMPLOYEE;
import static com.template.sbtemplate.integration.data.DbDataDto.EXISTING_PROJECT;
import static com.template.sbtemplate.integration.data.DbDataDto.NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS;
import static com.template.sbtemplate.integration.data.DbDataDto.NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS2;
import static com.template.sbtemplate.integration.data.DbDataDto.NEW_COMPLETE_EMPLOYEE_WITH_NEW_RELATIONS;
import static com.template.sbtemplate.integration.data.DbDataDto.NEW_EMPLOYEE;
import static com.template.sbtemplate.integration.data.DbDataDto.NO_EMAIL_EMPLOYEE;
import static com.template.sbtemplate.integration.data.DbDataDto.OTHER_ADDRESS;
import static com.template.sbtemplate.integration.data.DbDataDto.OTHER_DEPARTMENT;
import static com.template.sbtemplate.integration.data.DbDataDto.OTHER_PROJECT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIT extends TestContainers {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EmployeeRepository employeeRepository;

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
    void createNewEmployeeWithExistingRelations_shouldPersist() {
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
    void createNewEmployeeWithNewRelations_shouldPersist() {
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
    void createEmployee_withNullRelations_shouldPersist() {
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

    @Test
    void updateExistingEmployee_shouldPersist() {
        EmployeeDto updateDto = EXISTING_EMPLOYEE.toBuilder()
                .name("Updated Name")
                .department(OTHER_DEPARTMENT)
                .address(OTHER_ADDRESS)
                .projects(List.of(OTHER_PROJECT))
                .build();

        ResponseEntity<EmployeeDto> updateResponse = restTemplate
                .exchange("/employee/" + updateDto.getId(), HttpMethod.PUT, new HttpEntity<>(updateDto),
                        EmployeeDto.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        EmployeeDto returned = updateResponse.getBody();
        assertThat(returned).isNotNull();
        assertThat(returned.getName()).isEqualTo(updateDto.getName());

        checkRelations(returned.getId(), updateDto);
    }

    @Test
    void updateNewEmployee_shouldPersist() {
        ResponseEntity<EmployeeDto> updateResponse = restTemplate
                .exchange("/employee/" + NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS2.getId(), HttpMethod.PUT,
                        new HttpEntity<>(NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS2),
                        EmployeeDto.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        EmployeeDto returned = updateResponse.getBody();
        assertThat(returned).isNotNull();
        assertThat(returned.getId()).isNotNull();

        //check relations
        checkRelations(returned.getId(), NEW_COMPLETE_EMPLOYEE_WITH_EXISTING_RELATIONS2);
    }

    private void checkRelations(Long id, EmployeeDto updateDto) {
        Optional<Employee> savedOpt = employeeRepository.findFullById(id);
        assertThat(savedOpt).isPresent();
        Employee updated = savedOpt.get();

        assertThat(updated.getDepartment()).isNotNull();
        assertThat(updated.getDepartment().getId()).isEqualTo(updateDto.getDepartment().getId());
        assertThat(updated.getAddress()).isNotNull();
        assertThat(updated.getAddress().getId()).isEqualTo(updateDto.getAddress().getId());
        assertThat(updated.getProjects()).isNotEmpty();
        assertThat(updated.getProjects().getFirst()).isNotNull();
        assertThat(updated.getProjects().getFirst().getId()).isEqualTo(updateDto.getProjects().getFirst().getId());
    }

    @Test
    void updateEmployeeWithNewRelations_shouldReturnError() {
        EmployeeDto updateDto = EXISTING_EMPLOYEE.toBuilder()
                .name("Updated Name")
                .department(DepartmentDto.builder()
                        .id(99L)
                        .build())
                .address(AddressDto.builder()
                        .id(99L)
                        .build())
                .projects(List.of(ProjectDto.builder()
                        .id(99L)
                        .build()))
                .build();

        ResponseEntity<String> updateResponse = restTemplate
                .exchange("/employee/" + updateDto.getId(), HttpMethod.PUT, new HttpEntity<>(updateDto),
                        String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String returned = updateResponse.getBody();
        assertThat(returned).isNotNull();
        Optional<Employee> savedOpt = employeeRepository.findFullById(updateDto.getId());
        assertThat(savedOpt).isPresent();
    }

    @Test
    void deleteExistingEmployee_shouldReturnOk() {
        //create an employee to delete
        Employee employeeToDelete = Employee.builder()
                .email("todelete@delete.com")
                .name("To Delete")
                .phoneNumber("123456789")
                .build();
        Employee saved = employeeRepository.save(employeeToDelete);

        // When deleting the employee
        ResponseEntity<Void> response = restTemplate
                .exchange("/employee/" + saved.getId(), HttpMethod.DELETE, null, Void.class);

        // Then the response should be No Content
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // And the employee should no longer exist in the repository
        assertThat(employeeRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    void deleteNonExistingEmployee_shouldReturnOk() {
        // Given a non-existing employee ID
        long nonExistingId = 999L;

        // When trying to delete the non-existing employee
        ResponseEntity<Void> response = restTemplate
                .exchange("/employee/" + nonExistingId, HttpMethod.DELETE, null, Void.class);

        // Then the response should be Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(employeeRepository.existsById(nonExistingId)).isFalse();
    }
}
