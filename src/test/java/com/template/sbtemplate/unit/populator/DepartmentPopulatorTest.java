package com.template.sbtemplate.unit.populator;

import com.template.sbtemplate.domain.model.Department;
import com.template.sbtemplate.domain.repository.DepartmentRepository;
import com.template.sbtemplate.dto.DepartmentDto;
import com.template.sbtemplate.mapper.DepartmentMapper;
import com.template.sbtemplate.populator.DepartmentPopulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentPopulatorTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DepartmentMapper departmentMapper;
    @InjectMocks
    private DepartmentPopulator populator;

    @Test
    void populateNewOrExistingDepartment_whenDtoIsNull_returnsNull() {
        assertThat(populator.populateNewOrExistingDepartment(null)).isNull();
    }

    @Test
    void populateNewOrExistingDepartment_whenIdExists_returnsDepartmentById() {
        DepartmentDto dto = DepartmentDto.builder().id(1L).build();
        Department department = Department.builder().id(1L).build();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        assertThat(populator.populateNewOrExistingDepartment(dto)).isEqualTo(department);
        verify(departmentRepository).findById(1L);
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }

    @Test
    void populateNewOrExistingDepartment_whenIdNotFoundButCodeExists_returnsDepartmentByCode() {
        DepartmentDto dto = DepartmentDto.builder().id(2L).code("CODE").build();
        Department department = Department.builder().id(3L).code("CODE").build();
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());
        when(departmentRepository.findByCode("CODE")).thenReturn(Optional.of(department));
        assertThat(populator.populateNewOrExistingDepartment(dto)).isEqualTo(department);
        verify(departmentRepository).findById(2L);
        verify(departmentRepository).findByCode("CODE");
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }

    @Test
    void populateNewOrExistingDepartment_whenIdAndCodeNotFound_createsNewDepartment() {
        DepartmentDto dto = DepartmentDto.builder().id(4L).code("NEW").build();
        Department department = Department.builder().id(5L).code("NEW").build();
        when(departmentRepository.findById(4L)).thenReturn(Optional.empty());
        when(departmentRepository.findByCode("NEW")).thenReturn(Optional.empty());
        when(departmentMapper.toNewEntity(dto)).thenReturn(department);
        assertThat(populator.populateNewOrExistingDepartment(dto)).isEqualTo(department);
        verify(departmentRepository).findById(4L);
        verify(departmentRepository).findByCode("NEW");
        verify(departmentMapper).toNewEntity(dto);
    }

    @Test
    void populateNewOrExistingDepartment_whenIdAndCodeAreNull_createsNewDepartment() {
        DepartmentDto dto = DepartmentDto.builder().build();
        Department department = Department.builder().build();
        when(departmentMapper.toNewEntity(dto)).thenReturn(department);
        assertThat(populator.populateNewOrExistingDepartment(dto)).isEqualTo(department);
        verify(departmentMapper).toNewEntity(dto);
        verifyNoMoreInteractions(departmentRepository, departmentMapper);
    }

    @Test
    void populateExistingDepartment_whenDtoIsNull_returnsNull() {
        assertThat(populator.populateExistingDepartment(null)).isNull();
    }

    @Test
    void populateExistingDepartment_whenIdExists_returnsDepartment() {
        DepartmentDto dto = DepartmentDto.builder().id(10L).build();
        Department department = Department.builder().id(10L).build();
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(department));
        assertThat(populator.populateExistingDepartment(dto)).isEqualTo(department);
        verify(departmentRepository).findById(10L);
    }

    @Test
    void populateExistingDepartment_whenIdNotFound_throwsException() {
        DepartmentDto dto = DepartmentDto.builder().id(11L).build();
        when(departmentRepository.findById(11L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> populator.populateExistingDepartment(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Department with id 11 does not exist");
        verify(departmentRepository).findById(11L);
    }
}