package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Tag;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.model.User;
import com.taskflow.taskflow_backend.repository.TagRepository;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

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
        task.setTags(resolveTags(task.getTags()));
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
            task.setTags(resolveTags(updatedTask.getTags()));
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private Set<Tag> resolveTags(Set<Tag> tagsFromRequest) {
        Set<Tag> resolved = new HashSet<>();
        if (tagsFromRequest == null) {
            return resolved;
        }
        for (Tag tag : tagsFromRequest) {
            Tag existing = tagRepository.findByNom(tag.getNom()).orElseGet(() -> {
                Tag newTag = new Tag();
                newTag.setNom(tag.getNom());
                return tagRepository.save(newTag);
            });
            resolved.add(existing);
        }
        return resolved;
    }
}