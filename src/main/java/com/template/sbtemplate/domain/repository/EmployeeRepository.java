package com.template.sbtemplate.domain.repository;

import com.template.sbtemplate.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.address " +
            "LEFT JOIN FETCH e.department " +
            "LEFT JOIN FETCH e.projects " +
            "WHERE e.id = :id")
    Optional<Employee> findFullById(Long id);

    Optional<Employee> findBasicById(Long id);
}
