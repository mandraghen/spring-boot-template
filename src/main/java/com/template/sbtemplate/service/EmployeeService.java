// src/main/java/com/template/sbtemplate/service/EmployeeService.java
package com.template.sbtemplate.service;

import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.Scope;
import com.template.sbtemplate.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public Optional<EmployeeDto> get(Long id, Scope scope) {
        if (scope == null) {
            log.debug("Scope is null, returning response with basic view");
            return employeeRepository.findBasicById(id)
                    .map(employeeMapper::toDto);
        }
        return switch (scope) {
            case FULL -> employeeRepository.findFullById(id).map(entity ->
                    employeeMapper.toDto(entity, scope));
            case BASIC -> employeeRepository.findBasicById(id).map(entity ->
                    employeeMapper.toDto(entity, scope));
            default -> throw new IllegalArgumentException("Invalid scope: " + scope);
        };
    }

    @Transactional
    public Employee update(Long id, Employee updated) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        if (!existing.getVersion().equals(updated.getVersion())) {
            throw new RuntimeException("Version conflict");
        }
        existing.setName(updated.getName());
        // update other fields as needed
        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}