package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.dto.TaskResponse;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<TaskResponse> getAllTasks(Authentication authentication) {
        String email = authentication.getName();
        List<Task> tasks = taskService.getAllTasksForUser(email);
        return tasks.stream()
                .map(task -> new TaskResponse(task, taskService.estBloquee(task)))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        String email = authentication.getName();
        return taskService.createTask(task, email);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}