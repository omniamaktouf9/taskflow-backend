package com.taskflow.taskflow_backend.dto;

import com.taskflow.taskflow_backend.model.Task;
import lombok.Data;

@Data
public class TaskResponse {
    private Task task;
    private boolean bloquee;

    public TaskResponse(Task task, boolean bloquee) {
        this.task = task;
        this.bloquee = bloquee;
    }
}