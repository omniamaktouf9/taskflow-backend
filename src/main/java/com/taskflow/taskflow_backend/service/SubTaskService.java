package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.model.SubTask;
import com.taskflow.taskflow_backend.repository.SubTaskRepository;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubTaskService {

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    public SubTask addSubTask(Long taskId, String titre) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        SubTask subTask = new SubTask();
        subTask.setTitre(titre);
        subTask.setTask(task);
        return subTaskRepository.save(subTask);
    }

    public SubTask toggleSubTask(Long subTaskId) {
        SubTask subTask = subTaskRepository.findById(subTaskId).orElseThrow();
        subTask.setComplete(!subTask.isComplete());
        return subTaskRepository.save(subTask);
    }

    public void deleteSubTask(Long subTaskId) {
        subTaskRepository.deleteById(subTaskId);
    }
}