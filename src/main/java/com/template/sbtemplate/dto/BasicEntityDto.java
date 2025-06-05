package com.template.sbtemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BasicEntityDto {
    @EqualsAndHashCode.Include
    private Long id;
    private ZonedDateTime created;
    private ZonedDateTime updated;
    private Long version;
}
