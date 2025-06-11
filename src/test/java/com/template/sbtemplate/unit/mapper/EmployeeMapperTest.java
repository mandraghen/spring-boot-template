package com.template.sbtemplate.unit.mapper;

import com.template.sbtemplate.domain.model.Address;
import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.dto.AddressDto;
import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.Scope;
import com.template.sbtemplate.mapper.AddressMapper;
import com.template.sbtemplate.mapper.DepartmentMapper;
import com.template.sbtemplate.mapper.EmployeeMapperImpl;
import com.template.sbtemplate.populator.AddressPopulator;
import com.template.sbtemplate.populator.DepartmentPopulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {

    @Mock
    private Address address;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private DepartmentMapper departmentMapper;
    @Mock
    private AddressPopulator addressPopulator;
    @Mock
    private DepartmentPopulator departmentPopulator;
    @InjectMocks
    private EmployeeMapperImpl employeeMapper;

    @Test
    void toDto_whenNull_shouldReturnNull() {
        EmployeeDto result = employeeMapper.toDto(null);

        assertThat(result).isNull();
    }

    @Test
    void toDto_whenBasicScope_shouldMapBasicFields() {
        Employee employee = Employee.builder()
                .id(1L)
                .version(2L)
                .name("John")
                .build();

        EmployeeDto result = employeeMapper.toDto(employee, Scope.BASIC);

        compareEmployees(result, employee);
        assertThat(result.getAddress()).isNull();
    }

    @Test
    void toDto_whenFullScope_shouldMapAllFields() {
        Employee employee = Employee.builder()
                .id(1L)
                .version(2L)
                .name("John")
                .email("john@example.com")
                .address(address)
                .build();

        when(addressMapper.toDto(address)).thenReturn(AddressDto.builder().build());
        EmployeeDto result = employeeMapper.toDto(employee, Scope.FULL);

        compareEmployees(result, employee);
        assertThat(result.getAddress()).isNotNull();
    }

    @Test
    void toNewEntity_whenComplete_shouldMapAllFields() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("John")
                .email("john@example.com")
                .address(AddressDto.builder().build())
                .build();

        Employee result = employeeMapper.toNewEntity(dto);

        compareEmployees(result, dto);
    }

    @Test
    void toUpdateEntity_shouldSetIdAndMapFields() {
        EmployeeDto dto = EmployeeDto.builder()
                .name("John")
                .email("john@example.com")
                .build();

        Employee result = employeeMapper.toUpdateEntity(dto);

        compareEmployees(result, dto);
    }

    private void compareEmployees(EmployeeDto actual, Employee expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getVersion()).isEqualTo(expected.getVersion());
    }

    private void compareEmployees(Employee actual, EmployeeDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getVersion()).isEqualTo(expected.getVersion());
    }
}