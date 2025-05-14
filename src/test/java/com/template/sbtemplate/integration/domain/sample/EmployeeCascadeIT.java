package com.template.sbtemplate.integration.domain.sample;

import com.template.sbtemplate.domain.model.sample.Address;
import com.template.sbtemplate.domain.model.sample.Employee;
import com.template.sbtemplate.domain.repository.AddressRepository;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.integration.TestContainers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EmployeeCascadeIT extends TestContainers {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void testCascadePersist() {
        Address address = Address.builder().street("Main St").build();
        Employee employee = Employee.builder().name("John").address(address).build();
        employee = employeeRepository.save(employee);

        assertThat(employee.getAddress().getId()).isNotNull(); // Address is persisted
    }

    @Test
    void testCascadeMerge() {
        Address address = addressRepository.save(Address.builder().street("Old St").build());
        Employee employee = employeeRepository.save(Employee.builder().name("Jane").address(address).build());

        address.setStreet("New St");
        employee.setAddress(address);
        employeeRepository.save(employee);

        em.flush();
        em.clear();

        Address updated = addressRepository.findById(address.getId()).orElseThrow();
        assertThat(updated.getStreet()).isEqualTo("New St");
    }

    @Test
    void testCascadeRemove() {
        Address address = Address.builder().street("ToDelete").build();
        Employee employee = employeeRepository.save(Employee.builder().name("Del").address(address).build());

        address = employee.getAddress();

        employeeRepository.delete(employee);
        em.flush();

        assertThat(addressRepository.findById(address.getId())).isEmpty();
    }

    @Test
    void testCascadeDetach() {
        Address address = Address.builder().street("Detach St").build();
        Employee employee = employeeRepository.save(Employee.builder().name("Detach").address(address).build());

        em.detach(employee);

        assertThat(em.contains(address)).isFalse(); // Address is detached with Employee
    }

    @Test
    void testCascadeRefresh() {
        Address address = addressRepository.save(Address.builder().street("Refresh St").build());
        Employee employee = employeeRepository.save(Employee.builder().name("Refresh").address(address).build());

        address.setStreet("Changed St");
        em.refresh(employee);

        assertThat(employee.getAddress().getStreet()).isEqualTo("Refresh St");
    }
}