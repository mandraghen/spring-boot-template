package com.template.sbtemplate.unit.populator;

import com.template.sbtemplate.domain.model.Project;
import com.template.sbtemplate.domain.repository.ProjectRepository;
import com.template.sbtemplate.dto.ProjectDto;
import com.template.sbtemplate.mapper.ProjectMapper;
import com.template.sbtemplate.populator.ProjectPopulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectPopulatorTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @InjectMocks
    private ProjectPopulator populator;

    @Test
    void populateNewOrExistingProjects_whenDtoIsNull_returnsNull() {
        assertThat(populator.populateNewOrExistingProjects(null)).isNull();
    }

    @Test
    void populateNewOrExistingProjects_whenIdExists_returnsProjectById() {
        ProjectDto dto = ProjectDto.builder().id(1L).build();
        Project project = Project.builder().id(1L).build();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        assertThat(populator.populateNewOrExistingProjects(dto)).isEqualTo(project);
        verify(projectRepository).findById(1L);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void populateNewOrExistingProjects_whenIdNotFoundButCodeExists_returnsProjectByCode() {
        ProjectDto dto = ProjectDto.builder().id(2L).code("CODE").build();
        Project project = Project.builder().id(3L).code("CODE").build();
        when(projectRepository.findById(2L)).thenReturn(Optional.empty());
        when(projectRepository.findByCode("CODE")).thenReturn(Optional.of(project));
        assertThat(populator.populateNewOrExistingProjects(dto)).isEqualTo(project);
        verify(projectRepository).findById(2L);
        verify(projectRepository).findByCode("CODE");
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void populateNewOrExistingProjects_whenIdAndCodeNotFound_createsNewProject() {
        ProjectDto dto = ProjectDto.builder().id(4L).code("NEW").build();
        Project project = Project.builder().id(5L).code("NEW").build();
        when(projectRepository.findById(4L)).thenReturn(Optional.empty());
        when(projectRepository.findByCode("NEW")).thenReturn(Optional.empty());
        when(projectMapper.toNewEntity(dto)).thenReturn(project);
        assertThat(populator.populateNewOrExistingProjects(dto)).isEqualTo(project);
        verify(projectRepository).findById(4L);
        verify(projectRepository).findByCode("NEW");
        verify(projectMapper).toNewEntity(dto);
    }

    @Test
    void populateNewOrExistingProjects_whenIdAndCodeAreNull_createsNewProject() {
        ProjectDto dto = ProjectDto.builder().build();
        Project project = Project.builder().build();
        when(projectMapper.toNewEntity(dto)).thenReturn(project);
        assertThat(populator.populateNewOrExistingProjects(dto)).isEqualTo(project);
        verify(projectMapper).toNewEntity(dto);
        verifyNoMoreInteractions(projectRepository, projectMapper);
    }

    @Test
    void populateExistingProject_whenDtoIsNull_returnsNull() {
        assertThat(populator.populateExistingProject(null)).isNull();
    }

    @Test
    void populateExistingProject_whenIdExists_returnsProject() {
        ProjectDto dto = ProjectDto.builder().id(10L).build();
        Project project = Project.builder().id(10L).build();
        when(projectRepository.findById(10L)).thenReturn(Optional.of(project));
        assertThat(populator.populateExistingProject(dto)).isEqualTo(project);
        verify(projectRepository).findById(10L);
    }

    @Test
    void populateExistingProject_whenIdNotFound_throwsException() {
        ProjectDto dto = ProjectDto.builder().id(11L).build();
        when(projectRepository.findById(11L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> populator.populateExistingProject(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Project with id 11 does not exist");
        verify(projectRepository).findById(11L);
    }
}