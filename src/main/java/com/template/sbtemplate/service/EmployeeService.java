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
    public Optional<EmployeeDto> createOrUpdate(EmployeeDto employeeDto) {
        if (employeeDto.getId() != null && employeeRepository.existsById(employeeDto.getId()) ||
                employeeRepository.existsByEmail(employeeDto.getEmail())) {
            log.debug("Employee with id {} or email {} already exists", employeeDto.getId(), employeeDto.getEmail());
            return Optional.empty();
        }

        Employee employee = employeeMapper.toNewEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return Optional.of(employeeMapper.toDto(employee));
    }

    public Optional<EmployeeDto> get(Long id, Scope scope) {
        if (scope == null) {
            log.debug("Scope is null, returning response with basic view");
            return employeeRepository.findBasicById(id)
                    .map(employeeMapper::toDto);
        }
        //TODO Send the scope to the repository as well!
        return switch (scope) {
            case FULL -> employeeRepository.findFullById(id).map(entity ->
                    employeeMapper.toDto(entity, scope));
            case BASIC -> employeeRepository.findBasicById(id).map(entity ->
                    employeeMapper.toDto(entity, scope));
            default -> throw new IllegalArgumentException("Invalid scope: " + scope);
        };
    }

    //    @Transactional
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        employeeDto.setId(id);
        Employee updated = employeeMapper.toUpdateEntity(employeeDto);
        return employeeMapper.toDto(employeeRepository.save(updated));
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}