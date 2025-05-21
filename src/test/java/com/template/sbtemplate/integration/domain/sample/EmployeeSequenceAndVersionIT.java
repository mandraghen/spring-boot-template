package com.template.sbtemplate.integration.domain.sample;

import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.integration.TestContainers;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class EmployeeSequenceAndVersionIT extends TestContainers {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testSequenceGenerator() {
        Employee employee1 = employeeRepository.save(Employee.builder().name("Alice").build());
        Employee employee2 = employeeRepository.save(Employee.builder().name("Bob").build());

        assertThat(employee1.getId()).isNotNull();
        assertThat(employee2.getId()).isNotNull();
        assertThat(employee2.getId()).isGreaterThan(employee1.getId());
    }

    @Test
    void testVersionField() {
        Employee employee = employeeRepository.save(Employee.builder().name("Charlie").build());
        Long initialVersion = employee.getVersion();

        employee.setName("Charlie Updated");
        employee = employeeRepository.save(employee);

        assertThat(employee.getVersion()).isEqualTo(initialVersion + 1);
    }

    @Test
    void testVersionConflictThrowsException() {
        // Save initial employee
        Employee employee = employeeRepository.save(Employee.builder().name("Daisy").build());

        // Simulate two concurrent loads
        Employee e1 = employeeRepository.findById(employee.getId()).orElseThrow();
        Employee e2 = employeeRepository.findById(employee.getId()).orElseThrow();

        // First update succeeds
        e1.setName("Daisy 1");
        employeeRepository.save(e1);

        // Second update should fail due to version conflict
        e2.setName("Daisy 2");
        assertThatThrownBy(() -> employeeRepository.saveAndFlush(e2))
                .isInstanceOfAny(OptimisticLockingFailureException.class, OptimisticLockException.class);
    }
}
