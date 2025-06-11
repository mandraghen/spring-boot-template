package com.template.sbtemplate.unit.service;

import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.repository.EmployeeRepository;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.Scope;
import com.template.sbtemplate.mapper.EmployeeMapper;
import com.template.sbtemplate.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void create_whenDtoIsNull_shouldReturnEmpty() {
        Optional<EmployeeDto> result = employeeService.create(null);

        assertThat(result).isEmpty();
        verifyNoInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void create_whenIdExists_shouldReturnEmpty() {
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();
        when(employeeRepository.existsById(1L)).thenReturn(true);

        Optional<EmployeeDto> result = employeeService.create(dto);

        assertThat(result).isEmpty();
        verify(employeeRepository).existsById(1L);
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void create_whenEmailExists_shouldReturnEmpty() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("Test")
                .email("test@example.com")
                .build();
        when(employeeRepository.existsByEmail("test@example.com")).thenReturn(true);

        Optional<EmployeeDto> result = employeeService.create(dto);

        assertThat(result).isEmpty();
        verify(employeeRepository).existsByEmail("test@example.com");
        verifyNoMoreInteractions(employeeRepository, employeeMapper);
    }

    @Test
    void create_whenValid_shouldReturnDto() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("John Doe")
                .email("test@example.com")
                .build();
        Employee entity = Employee.builder()
                .name("John Doe")
                .email("test@example.com")
                .build();
        Employee saved = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("test@example.com")
                .build();

        when(employeeRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(employeeMapper.toNewEntity(dto)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(saved);
        when(employeeMapper.toDto(saved)).thenReturn(dto);

        Optional<EmployeeDto> result = employeeService.create(dto);

        assertThat(result).contains(dto);
        verify(employeeRepository).existsByEmail("test@example.com");
        verify(employeeMapper).toNewEntity(dto);
        verify(employeeRepository).save(entity);
        verify(employeeMapper).toDto(saved);
    }

    @Test
    void get_whenNullIdAndNoScope_shouldReturnEmpty() {
        when(employeeRepository.find(null, null)).thenReturn(Optional.empty());

        Optional<EmployeeDto> result = employeeService.get(null, null);

        assertThat(result).isEmpty();
        verify(employeeRepository).find(null, null);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void get_whenIdNotFoundAndNoScope_shouldReturnEmpty() {
        when(employeeRepository.find(1L, null)).thenReturn(Optional.empty());

        Optional<EmployeeDto> result = employeeService.get(1L, null);

        assertThat(result).isEmpty();
        verify(employeeRepository).find(1L, null);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void get_whenIdFoundAndNoScope_shouldReturnBasicDto() {
        Employee employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .build();
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("John Doe")
                .build();

        when(employeeRepository.find(1L, null)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee, null)).thenReturn(dto);

        Optional<EmployeeDto> result = employeeService.get(1L, null);

        assertThat(result).contains(dto);
        verify(employeeRepository).find(1L, null);
        verify(employeeMapper).toDto(employee, null);
    }

    @Test
    void get_whenIdFoundAndFullScope_shouldReturnFullDto() {
        Employee employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .build();
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("John Doe")
                .build();

        when(employeeRepository.find(1L, Scope.FULL)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee, Scope.FULL)).thenReturn(dto);

        Optional<EmployeeDto> result = employeeService.get(1L, Scope.FULL);

        assertThat(result).contains(dto);
        verify(employeeRepository).find(1L, Scope.FULL);
        verify(employeeMapper).toDto(employee, Scope.FULL);
    }

    @Test
    void get_whenIdFoundAndBasicScope_shouldReturnBasicDto() {
        Employee employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .build();
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("John Doe")
                .build();

        when(employeeRepository.find(1L, Scope.BASIC)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee, Scope.BASIC)).thenReturn(dto);

        Optional<EmployeeDto> result = employeeService.get(1L, Scope.BASIC);

        assertThat(result).contains(dto);
        verify(employeeRepository).find(1L, Scope.BASIC);
        verify(employeeMapper).toDto(employee, Scope.BASIC);
    }

    @Test
    void get_whenIdFoundAndIdOnlyScope_shouldReturnIdOnlyDto() {
        Employee employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .build();
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("John Doe")
                .build();

        when(employeeRepository.find(1L, Scope.ID_ONLY)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee, Scope.ID_ONLY)).thenReturn(dto);

        Optional<EmployeeDto> result = employeeService.get(1L, Scope.ID_ONLY);

        assertThat(result).contains(dto);
        verify(employeeRepository).find(1L, Scope.ID_ONLY);
        verify(employeeMapper).toDto(employee, Scope.ID_ONLY);
    }

    @Test
    void update_whenDtoIsNull_shouldThrowException() {
        assertThatThrownBy(() -> employeeService.update(1L, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Cannot invoke \"com.template.sbtemplate.dto.EmployeeDto.setId(java.lang.Long)\" because \"employeeDto\" is null");
    }

    @Test
    void update_whenIdIsNull_shouldThrowException() {
        EmployeeDto dto = EmployeeDto.builder().name("Test").build();

        when(employeeRepository.findBasicById(null)).thenThrow(NullPointerException.class);

        assertThatThrownBy(() -> employeeService.update(null, dto))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void update_whenEmployeeNotFound_shouldSetIdToNull() {
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("John Doe")
                .email("test@example.com")
                .build();

        Employee mappedEmployee = Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

        Employee persistedEmployee = mappedEmployee.toBuilder().id(2L).build();

        when(employeeRepository.findBasicById(1L)).thenReturn(Optional.empty());
        when(employeeMapper.toUpdateEntity(dto.toBuilder().id(null).build())).thenReturn(mappedEmployee);

        when(employeeRepository.save(any(Employee.class))).thenReturn(persistedEmployee);
        when(employeeMapper.toDto(persistedEmployee)).thenReturn(dto.toBuilder().id(2L).build());

        EmployeeDto result = employeeService.update(1L, dto);

        assertThat(result.getId()).isNotNull();
        verify(employeeRepository).findBasicById(1L);
        verify(employeeMapper).toUpdateEntity(dto.toBuilder().id(null).build());
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toDto(any(Employee.class));
    }

    @Test
    void update_whenEmployeeFound_shouldUpdateEmployee() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("John Doe")
                .email("test@example.com")
                .version(1L)
                .build();
        Employee existingEmployee = Employee.builder()
                .id(1L)
                .name("Old Name")
                .email("old@example.com")
                .version(1L)
                .build();
        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("test@example.com")
                .version(1L)
                .build();

        when(employeeRepository.findBasicById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeMapper.toUpdateEntity(dto)).thenReturn(updatedEmployee);
        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toDto(updatedEmployee)).thenReturn(dto);

        EmployeeDto result = employeeService.update(1L, dto);

        assertThat(result).isEqualTo(dto);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getVersion()).isEqualTo(1);
        verify(employeeRepository).findBasicById(1L);
        verify(employeeMapper).toUpdateEntity(dto);
        verify(employeeRepository).save(updatedEmployee);
        verify(employeeMapper).toDto(updatedEmployee);
    }

    @Test
    void delete_shouldCallRepository() {
        employeeService.delete(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void delete_whenIdIsNull_shouldNotCallRepository() {
        employeeService.delete(null);
        verify(employeeRepository, never()).deleteById(any());
    }
}