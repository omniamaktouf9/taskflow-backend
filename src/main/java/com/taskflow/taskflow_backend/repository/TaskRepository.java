package com.taskflow.taskflow_backend.repository;

import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserAndProjectId(User user, Long projectId);
    List<Task> findByDateEcheanceAndStatutNot(String dateEcheance, String statut);
}