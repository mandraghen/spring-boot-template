package com.template.sbtemplate.mapper;

import com.template.sbtemplate.domain.model.BasicEntity;
import com.template.sbtemplate.dto.BasicEntityDto;
import com.template.sbtemplate.dto.Scope;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

public interface GenericMapper<S extends BasicEntity, T extends BasicEntityDto> {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id")
    @Mapping(target = "created")
    @Mapping(target = "updated")
    @Mapping(target = "version")
    @Named("idOnly")
    T entityToIdOnlyDto(S entity);

    @Named("basic")
    T entityToBasicDto(S entity);

    @Named("full")
    T entityToFullDto(S entity);

    default T toDto(S entity, Scope scope) {
        return switch (scope) {
            case ID_ONLY -> entityToIdOnlyDto(entity);
            case BASIC -> entityToBasicDto(entity);
            case FULL -> entityToFullDto(entity);
        };
    }

    default T toDto(S entity) {
        return toDto(entity, Scope.BASIC);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Named("newEntity")
    S toNewEntity(T dto);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Named("updateEntity")
    S toUpdateEntity(T dto);
}
