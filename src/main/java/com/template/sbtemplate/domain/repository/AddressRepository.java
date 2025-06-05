package com.template.sbtemplate.domain.repository;

import com.template.sbtemplate.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // Custom query methods can be defined here if needed
}
