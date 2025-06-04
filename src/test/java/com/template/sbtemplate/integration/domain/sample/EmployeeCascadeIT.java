package com.template.sbtemplate.integration.domain.sample;

import com.template.sbtemplate.domain.model.Address;
import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.model.Project;
import com.template.sbtemplate.domain.repository.AddressRepository;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.domain.repository.ProjectRepository;
import com.template.sbtemplate.integration.TestContainers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeCascadeIT extends TestContainers {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void testAddressCascadePersist() {
        Address address = Address.builder().street("Main St").build();
        Employee employee = Employee.builder().name("John").address(address).build();
        employee = employeeRepository.save(employee);

        assertThat(employee.getAddress().getId()).isNotNull(); // Address is persisted
    }

    @Test
    @Transactional
    void testAddressCascadeMerge() {
        Address address = addressRepository.save(Address.builder().street("Old St").build());
        Employee employee = employeeRepository.save(Employee.builder().name("Jane").address(address).build());

        address.setStreet("New St");
        employee.setAddress(address);
        employeeRepository.save(employee);

        Address updated = addressRepository.findById(address.getId()).orElseThrow();
        assertThat(updated.getStreet()).isEqualTo("New St");
    }

    @Test
    void testAddressCascadeRemove() {
        Address address = Address.builder().street("ToDelete").build();
        Employee employee = employeeRepository.save(Employee.builder().name("Del").address(address).build());

        address = employee.getAddress();

        employeeRepository.delete(employee);

        assertThat(addressRepository.findById(address.getId())).isEmpty();
    }

    @Test
    void testAddressCascadeDetach() {
        Address address = Address.builder().street("Detach St").build();
        Employee employee = employeeRepository.save(Employee.builder().name("Detach").address(address).build());

        em.detach(employee);

        assertThat(em.contains(address)).isFalse(); // Address is detached with Employee
    }

    @Test
    @Transactional
    void testAddressCascadeRefresh() {
        Address address = addressRepository.save(Address.builder().street("Refresh St").build());
        Employee employee = employeeRepository.save(Employee.builder().name("Refresh").address(address).build());

        address.setStreet("Changed St");
        em.refresh(employee);

        assertThat(employee.getAddress().getStreet()).isEqualTo("Refresh St");
    }

    @Test
    void testProjectCascadePersist() {
        Project project = Project.builder().code("P1").name("Project 1").build();
        ArrayList<Project> projects = new ArrayList<>(List.of(project));
        Employee employee = Employee.builder().name("John").projects(projects).build();
        employee = employeeRepository.save(employee);

        project = employee.getProjects().getFirst();
        assertThat(project).isNotNull(); // Project is persisted
        assertThat(projectRepository.findById(project.getId())).isPresent();
    }

    @Test
    @Transactional
    void testProjectCascadeMerge() {
        Project project = projectRepository.save(Project.builder().code("P2").name("Project 2").build());
        ArrayList<Project> projects = new ArrayList<>(List.of(project));
        Employee employee = employeeRepository.save(Employee.builder().name("Jane").projects(projects).build());

        project.setName("Project 2 Updated");
        employee = employeeRepository.save(employee);

        project = employee.getProjects().getFirst();
        Project updated = projectRepository.findById(project.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Project 2 Updated");
    }

    @Test
    @Transactional
    void testProjectCascadeRemove() {
        Project project = projectRepository.save(Project.builder().code("P3").name("Project 3").build());
        ArrayList<Project> projects = new ArrayList<>(List.of(project));
        Employee employee = employeeRepository.save(Employee.builder().name("Del").projects(projects).build());

        employeeRepository.delete(employee);

        // Project should not be deleted (no REMOVE cascade), but the relation should be removed
        assertThat(projectRepository.findById(project.getId())).isPresent();
    }

    @Test
    @Transactional
    void testAddressOrphanRemoval() {
        Address address = addressRepository.save(Address.builder().street("Orphan St").build());
        Employee employee = employeeRepository.save(Employee.builder().name("Orphan").address(address).build());

        // Remove the address from the employee
        employee.setAddress(null);
        employeeRepository.saveAndFlush(employee);

        // Address should be deleted from the database
        assertThat(addressRepository.findById(address.getId())).isEmpty();
    }

    @Test
    @Transactional
    void testAddressReassignmentBetweenEmployees() {
        // Create and persist two employees, each with their own address
        Address address1 = addressRepository.save(Address.builder().street("First St").build());
        Address address2 = addressRepository.save(Address.builder().street("Second St").build());
        Employee employee1 = employeeRepository.save(Employee.builder().name("Emp1").address(address1).build());
        Employee employee2 = employeeRepository.save(Employee.builder().name("Emp2").address(address2).build());

        em.refresh(address1);
        // Reassign address1 from employee1 to employee2
        employee2.setAddress(address1);
        employeeRepository.saveAndFlush(employee2);

        // Refresh entities
        em.refresh(employee1);
        em.refresh(employee2);

        // employee2 should now have address1
        assertThat(employee2.getAddress().getId()).isEqualTo(address1.getId());
        // employee1 should have no address
        assertThat(employee1.getAddress()).isNull();
        // address2 is deleted because of orphan removal
        assertThat(addressRepository.findById(address2.getId())).isNotPresent();
    }
}
