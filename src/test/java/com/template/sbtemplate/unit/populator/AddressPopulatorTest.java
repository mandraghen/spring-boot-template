package com.template.sbtemplate.unit.populator;

import com.template.sbtemplate.domain.model.Address;
import com.template.sbtemplate.domain.model.Employee;
import com.template.sbtemplate.domain.repository.AddressRepository;
import com.template.sbtemplate.dto.AddressDto;
import com.template.sbtemplate.mapper.AddressMapper;
import com.template.sbtemplate.populator.AddressPopulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressPopulatorTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressPopulator addressPopulator;

    @Test
    void populateNewOrExistingAddress_whenDtoIsNull_shouldReturnNull() {
        Address result = addressPopulator.populateNewOrExistingAddress(null);

        assertThat(result).isNull();
        verifyNoInteractions(addressRepository, addressMapper);
    }

    @Test
    void populateNewOrExistingAddress_whenIdIsNull_shouldCreateNew() {
        AddressDto dto = AddressDto.builder()
                .street("Test Street")
                .city("Test City")
                .build();
        Address newAddress = Address.builder().build();
        when(addressMapper.toNewEntity(dto)).thenReturn(newAddress);

        Address result = addressPopulator.populateNewOrExistingAddress(dto);

        assertThat(result).isSameAs(newAddress);
        verify(addressMapper).toNewEntity(dto);
        verifyNoInteractions(addressRepository);
    }

    @Test
    void populateNewOrExistingAddress_whenExistingWithoutEmployee_shouldReturnExisting() {
        AddressDto dto = AddressDto.builder()
                .id(1L)
                .build();
        Address existingAddress = Address.builder()
                .id(1L)
                .build();
        when(addressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));

        Address result = addressPopulator.populateNewOrExistingAddress(dto);

        assertThat(result).isSameAs(existingAddress);
        verify(addressRepository).findById(1L);
        verifyNoInteractions(addressMapper);
    }

    @Test
    void populateNewOrExistingAddress_whenExistingWithEmployee_shouldDetachEmployee() {
        AddressDto dto = AddressDto.builder()
                .id(1L)
                .build();
        Employee employee = Employee.builder().build();
        Address existingAddress = Address.builder()
                .id(1L)
                .employee(employee)
                .build();
        employee.setAddress(existingAddress);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));

        Address result = addressPopulator.populateNewOrExistingAddress(dto);

        assertThat(result).isSameAs(existingAddress);
        assertThat(existingAddress.getEmployee().getAddress()).isNull();
        verify(addressRepository).findById(1L);
        verifyNoInteractions(addressMapper);
    }

    @Test
    void populateNewOrExistingAddress_whenNotFound_shouldCreateNew() {
        AddressDto dto = AddressDto.builder()
                .id(1L)
                .build();
        Address newAddress = Address.builder().build();
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        when(addressMapper.toNewEntity(dto)).thenReturn(newAddress);

        Address result = addressPopulator.populateNewOrExistingAddress(dto);

        assertThat(result).isSameAs(newAddress);
        verify(addressRepository).findById(1L);
        verify(addressMapper).toNewEntity(dto);
    }

    @Test
    void populateExistingAddress_whenDtoIsNull_shouldReturnNull() {
        Address result = addressPopulator.populateExistingAddress(null);

        assertThat(result).isNull();
        verifyNoInteractions(addressRepository, addressMapper);
    }

    @Test
    void populateExistingAddress_whenIdIsNull_shouldThrowException() {
        AddressDto dto = AddressDto.builder().build();

        assertThatThrownBy(() -> addressPopulator.populateExistingAddress(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Address with id null does not exist.");

        verifyNoInteractions(addressRepository, addressMapper);
    }

    @Test
    void populateExistingAddress_whenExistingWithoutEmployee_shouldReturnExisting() {
        AddressDto dto = AddressDto.builder()
                .id(1L)
                .build();
        Address existingAddress = Address.builder()
                .id(1L)
                .build();
        when(addressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));

        Address result = addressPopulator.populateExistingAddress(dto);

        assertThat(result).isSameAs(existingAddress);
        verify(addressRepository).findById(1L);
        verifyNoInteractions(addressMapper);
    }

    @Test
    void populateExistingAddress_whenExistingWithEmployee_shouldDetachEmployee() {
        AddressDto dto = AddressDto.builder()
                .id(1L)
                .build();
        Employee employee = Employee.builder().build();
        Address existingAddress = Address.builder()
                .id(1L)
                .employee(employee)
                .build();
        employee.setAddress(existingAddress);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));

        Address result = addressPopulator.populateExistingAddress(dto);

        assertThat(result).isSameAs(existingAddress);
        assertThat(existingAddress.getEmployee().getAddress()).isNull();
        verify(addressRepository).findById(1L);
        verifyNoInteractions(addressMapper);
    }

    @Test
    void populateExistingAddress_whenNotFound_shouldThrowException() {
        AddressDto dto = AddressDto.builder()
                .id(1L)
                .build();
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressPopulator.populateExistingAddress(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Address with id 1 does not exist.");

        verify(addressRepository).findById(1L);
        verifyNoInteractions(addressMapper);
    }
}