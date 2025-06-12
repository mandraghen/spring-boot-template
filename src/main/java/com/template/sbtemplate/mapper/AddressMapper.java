package com.template.sbtemplate.mapper;

import com.template.sbtemplate.domain.model.Address;
import com.template.sbtemplate.dto.AddressDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface AddressMapper extends GenericScopedMapper<Address, AddressDto> {

    @BeanMapping(ignoreByDefault = true)
    @InheritConfiguration(name = "entityToIdOnlyDto")
    @Mapping(target = "street")
    @Mapping(target = "city")
    @Mapping(target = "state")
    @Mapping(target = "postalCode")
    @Mapping(target = "country")
    @Named("addressBasic")
    @Override
    AddressDto entityToBasicDto(Address address);
}