package com.template.sbtemplate.populator;

import com.template.sbtemplate.domain.model.Department;
import com.template.sbtemplate.domain.repository.DepartmentRepository;
import com.template.sbtemplate.dto.DepartmentDto;
import com.template.sbtemplate.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DepartmentPopulator {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    /**
     * Populates a new or existing Department entity based on the provided DepartmentDto.
     * If the dto has an id, it will try to find the department by id.
     * If not found, it will try to find it by code.
     * If still not found, it will create a new Department entity from the dto.
     *
     * @param departmentDto the DepartmentDto to populate
     * @return an Optional containing the populated Department entity
     */
    @Named("newOrExistingDepartment")
    public Department populateNewOrExistingDepartment(DepartmentDto departmentDto) {
        return Optional.ofNullable(departmentDto)
                .map(dto -> Optional.ofNullable(dto.getId())
                        .flatMap(departmentRepository::findById)
                        .orElseGet(() -> Optional.ofNullable(dto.getCode())
                                .flatMap(departmentRepository::findByCode)
                                .orElseGet(() -> departmentMapper.toNewEntity(dto))))
                .orElse(null);
    }

    /**
     * Populates an existing Department entity based on the provided DepartmentDto.
     * If the dto has an id, it will try to find the department by id.
     * If not found, it will throw an IllegalArgumentException.
     *
     * @param departmentDto the DepartmentDto to populate
     * @return the populated Department entity
     */
    @Named("existingDepartment")
    public Department populateExistingDepartment(DepartmentDto departmentDto) {
        return Optional.ofNullable(departmentDto)
                .map(dto -> Optional.ofNullable(dto.getId())
                        .flatMap(departmentRepository::findById)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Department with id " + dto.getId() + " does not exist.")))
                .orElse(null);
    }
}
