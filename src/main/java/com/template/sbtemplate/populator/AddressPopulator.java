package com.template.sbtemplate.populator;

import com.template.sbtemplate.domain.model.Address;
import com.template.sbtemplate.domain.repository.AddressRepository;
import com.template.sbtemplate.dto.AddressDto;
import com.template.sbtemplate.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AddressPopulator {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Named("newOrExistingAddress")
    public Address populateNewOrExistingAddress(AddressDto addressDto) {
        return Optional.ofNullable(addressDto)
                .map(dto -> Optional.ofNullable(addressDto.getId())
                        .flatMap(this::findAndDetachEmployee)
                        .orElseGet(() -> addressMapper.toNewEntity(dto)))
                .orElse(null);
    }

    @Named("existingAddress")
    public Address populateExistingAddress(AddressDto addressDto) {
        return Optional.ofNullable(addressDto)
                .map(dto -> Optional.ofNullable(dto.getId())
                        .flatMap(this::findAndDetachEmployee)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Address with id " + dto.getId() + " does not exist.")))
                .orElse(null);
    }

    private Optional<Address> findAndDetachEmployee(Long id) {
        return addressRepository.findById(id).map(address -> {
            if (address.getEmployee() != null) {
                address.getEmployee().setAddress(null);
            }
            return address;
        });
    }
}
