package com.template.sbtemplate.domain.model.sample;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(
        indexes = {@Index(name = "department_code_index", columnList = "code", unique = true)}
)
public class Department extends AbstractEntity {
    @SequenceGenerator(name = "department_id_seq", sequenceName = "department_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
    @Column(updatable = false)
    private Long id;

    private String code;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
