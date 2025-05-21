package com.template.sbtemplate.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BasicEntityDto {
    @EqualsAndHashCode.Include
    private Long id;
    private ZonedDateTime created;
    private ZonedDateTime updated;
    private Long version;
}
