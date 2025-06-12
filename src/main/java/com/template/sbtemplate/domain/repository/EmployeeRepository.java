package com.template.sbtemplate.domain.repository;

import com.template.sbtemplate.domain.model.Employee;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface EmployeeRepository extends ScopedRepository<Employee, Long> {
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.address " +
            "LEFT JOIN FETCH e.department " +
            "LEFT JOIN FETCH e.projects " +
            "WHERE e.id = :id")
    Optional<Employee> findFullById(Long id);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Employee> findBasicById(Long id);

    boolean existsByEmail(String email);
}
