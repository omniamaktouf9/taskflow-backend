package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.model.User;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTasksForUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return taskRepository.findByUser(user);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setTitre(updatedTask.getTitre());
            task.setDescription(updatedTask.getDescription());
            task.setStatut(updatedTask.getStatut());
            task.setPriorite(updatedTask.getPriorite());
            task.setDateEcheance(updatedTask.getDateEcheance());
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}