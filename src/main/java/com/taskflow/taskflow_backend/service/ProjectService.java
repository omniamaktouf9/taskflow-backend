package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Project;
import com.taskflow.taskflow_backend.model.User;
import com.taskflow.taskflow_backend.repository.ProjectRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Project> getAllProjectsForUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return projectRepository.findByUser(user);
    }

    public Project createProject(Project project, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        project.setUser(user);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            project.setNom(updatedProject.getNom());
            project.setDescription(updatedProject.getDescription());
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}