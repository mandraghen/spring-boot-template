package com.template.sbtemplate.unit.controller;

import com.template.sbtemplate.controller.EmployeeController;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.Scope;
import com.template.sbtemplate.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getEmployee_whenFound_shouldReturnOk() {
        EmployeeDto dto = EmployeeDto.builder()
                .id(1L)
                .name("John")
                .build();
        when(employeeService.get(1L, Scope.FULL)).thenReturn(Optional.of(dto));

        ResponseEntity<EmployeeDto> response = employeeController.get(1L, Scope.FULL);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
        verify(employeeService).get(1L, Scope.FULL);
    }

    @Test
    void getEmployee_whenNotFound_shouldReturnNotFound() {
        when(employeeService.get(1L, null)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDto> response = employeeController.get(1L, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(employeeService).get(1L, null);
    }

    @Test
    void createEmployee_whenSuccess_shouldReturnOk() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("John")
                .build();
        when(employeeService.create(dto)).thenReturn(Optional.of(dto));

        ResponseEntity<EmployeeDto> response = employeeController.create(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
        verify(employeeService).create(dto);
    }

    @Test
    void createEmployee_whenFailure_shouldReturnBadRequest() {
        EmployeeDto dto = EmployeeDto.builder().build();
        when(employeeService.create(dto)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDto> response = employeeController.create(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(employeeService).create(dto);
    }

    @Test
    void updateEmployee_shouldReturnOk() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("John")
                .build();
        EmployeeDto updated = EmployeeDto.builder()
                .id(1L)
                .name("John")
                .build();
        when(employeeService.update(1L, dto)).thenReturn(updated);

        ResponseEntity<EmployeeDto> response = employeeController.update(1L, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updated);
        verify(employeeService).update(1L, dto);
    }

    @Test
    void deleteEmployee_shouldReturnNoContent() {
        doNothing().when(employeeService).delete(1L);

        ResponseEntity<Void> response = employeeController.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(employeeService).delete(1L);
    }
}