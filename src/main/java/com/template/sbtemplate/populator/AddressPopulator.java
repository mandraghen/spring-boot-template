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
                        .flatMap(addressRepository::findById)
                        .orElseGet(() -> addressMapper.toNewEntity(dto)))
                .orElse(null);
    }
}
