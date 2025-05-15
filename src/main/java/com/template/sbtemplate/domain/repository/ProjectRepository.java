package com.template.sbtemplate.domain.repository;

import com.template.sbtemplate.domain.model.sample.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom query methods can be defined here if needed
}
