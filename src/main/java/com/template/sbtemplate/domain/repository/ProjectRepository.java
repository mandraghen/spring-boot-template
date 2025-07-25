package com.template.sbtemplate.domain.repository;

import com.template.sbtemplate.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByCode(String code);
}
