package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.dto.SubTaskRequest;
import com.taskflow.taskflow_backend.model.SubTask;
import com.taskflow.taskflow_backend.service.SubTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtasks")
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @PostMapping("/task/{taskId}")
    public SubTask addSubTask(@PathVariable Long taskId, @RequestBody SubTaskRequest request) {
        return subTaskService.addSubTask(taskId, request.getTitre());
    }

    @PutMapping("/{id}/toggle")
    public SubTask toggleSubTask(@PathVariable Long id) {
        return subTaskService.toggleSubTask(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSubTask(@PathVariable Long id) {
        subTaskService.deleteSubTask(id);
    }
}