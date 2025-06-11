package com.template.sbtemplate.service;

import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.Scope;
import com.template.sbtemplate.mapper.EmployeeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public Optional<EmployeeDto> create(EmployeeDto employeeDto) {
        if (employeeDto == null ||
                employeeDto.getId() != null && employeeRepository.existsById(employeeDto.getId()) ||
                employeeRepository.existsByEmail(employeeDto.getEmail())) {
            log.debug("Employee already exists by email or id or is null: {}", employeeDto);
            return Optional.empty();
        }

        Employee employee = employeeMapper.toNewEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return Optional.of(employeeMapper.toDto(employee));
    }

    public Optional<EmployeeDto> get(Long id, Scope scope) {
        return employeeRepository.find(id, scope)
                .map(employee -> employeeMapper.toDto(employee, scope));
    }

    @Transactional
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        employeeRepository.findBasicById(id).ifPresentOrElse(existingEmployee -> {
            employeeDto.setId(existingEmployee.getId());
            if (employeeDto.getVersion() == null) {
                employeeDto.setVersion(existingEmployee.getVersion());
            }
        }, () -> employeeDto.setId(null));

        Employee updated = employeeMapper.toUpdateEntity(employeeDto);
        return employeeMapper.toDto(employeeRepository.save(updated));
    }

    public void delete(Long id) {
        if (id == null) {
            return;
        }
        employeeRepository.deleteById(id);
    }
}