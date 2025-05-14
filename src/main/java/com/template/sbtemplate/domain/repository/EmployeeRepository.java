package com.template.sbtemplate.domain.repository;

import com.template.sbtemplate.domain.model.sample.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Custom query methods can be defined here if needed
}
