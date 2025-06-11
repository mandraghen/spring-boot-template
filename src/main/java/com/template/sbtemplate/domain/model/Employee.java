package com.template.sbtemplate.domain.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(
        indexes = {@Index(name = "employee_email_index", columnList = "email", unique = true)}
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
public class Employee extends BasicEntity {
    @SequenceGenerator(name = "employee_id_seq", sequenceName = "employee_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_seq")
    @Column(updatable = false)
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
    private Address address;

    @Setter(AccessLevel.NONE)
    @Column(name = "address_id", insertable = false, updatable = false)
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "department_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
    private Department department;

    @Setter(AccessLevel.NONE)
    @Column(name = "department_id", insertable = false, updatable = false)
    private Long departmentId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "defaultCache")
    private List<Project> projects;

    public void setAddress(Address address) {
        if (Objects.nonNull(address)) {
            if (Objects.nonNull(address.getEmployee()) && address.getEmployee() != this) {
                address.getEmployee().setAddress(null);
            }
            address.setEmployee(this);
        }
        this.address = address;
    }
}
