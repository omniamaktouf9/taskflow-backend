package com.taskflow.taskflow_backend.repository;

import com.taskflow.taskflow_backend.model.Project;
import com.taskflow.taskflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
}