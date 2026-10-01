package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Project;
import com.taskflow.taskflow_backend.model.Tag;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.model.TaskActivity;
import com.taskflow.taskflow_backend.model.User;
import com.taskflow.taskflow_backend.repository.ProjectRepository;
import com.taskflow.taskflow_backend.repository.TagRepository;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<Task> getAllTasksForUser(String email, Long projectId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (projectId != null) {
            return taskRepository.findByUserAndProjectId(user, projectId);
        }
        return taskRepository.findByUser(user);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        task.setUser(user);
        task.setTags(resolveTags(task.getTags()));
        task.setDependencies(resolveDependencies(task.getDependencies()));
        resolveProject(task);

        TaskActivity activity = new TaskActivity();
        activity.setAction("Tâche créée");
        activity.setTask(task);
        task.getActivities().add(activity);

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            ajouterActiviteSiChange(task, "Titre", task.getTitre(), updatedTask.getTitre());
            ajouterActiviteSiChange(task, "Statut", task.getStatut(), updatedTask.getStatut());
            ajouterActiviteSiChange(task, "Priorité", task.getPriorite(), updatedTask.getPriorite());
            ajouterActiviteSiChange(task, "Date d'échéance", task.getDateEcheance(), updatedTask.getDateEcheance());

            task.setTitre(updatedTask.getTitre());
            task.setDescription(updatedTask.getDescription());
            task.setStatut(updatedTask.getStatut());
            task.setPriorite(updatedTask.getPriorite());
            task.setDateEcheance(updatedTask.getDateEcheance());
            task.setTags(resolveTags(updatedTask.getTags()));
            task.setDependencies(resolveDependencies(updatedTask.getDependencies()));

            resolveProject(updatedTask);
            task.setProject(updatedTask.getProject());

            return taskRepository.save(task);
        }
        return null;
    }

    private void resolveProject(Task task) {
        if (task.getProject() != null && task.getProject().getId() != null) {
            Project fullProject = projectRepository.findById(task.getProject().getId()).orElse(null);
            task.setProject(fullProject);
        } else {
            task.setProject(null);
        }
    }

    private Set<Task> resolveDependencies(Set<Task> dependenciesFromRequest) {
        Set<Task> resolved = new HashSet<>();
        if (dependenciesFromRequest == null) {
            return resolved;
        }
        for (Task dep : dependenciesFromRequest) {
            if (dep.getId() != null) {
                taskRepository.findById(dep.getId()).ifPresent(resolved::add);
            }
        }
        return resolved;
    }

    public boolean estBloquee(Task task) {
        if (task.getDependencies() == null || task.getDependencies().isEmpty()) {
            return false;
        }
        return task.getDependencies().stream()
                .anyMatch(dep -> !"TERMINE".equals(dep.getStatut()));
    }

    public List<String> getDependancesNonTerminees(Task task) {
        if (task.getDependencies() == null) {
            return List.of();
        }
        return task.getDependencies().stream()
                .filter(dep -> !"TERMINE".equals(dep.getStatut()))
                .map(Task::getTitre)
                .collect(Collectors.toList());
    }

    private void ajouterActiviteSiChange(Task task, String champ, String ancienneValeur, String nouvelleValeur) {
        if (ancienneValeur == null || !ancienneValeur.equals(nouvelleValeur)) {
            TaskActivity activity = new TaskActivity();
            activity.setAction(champ + " modifié");
            activity.setAncienneValeur(ancienneValeur);
            activity.setNouvelleValeur(nouvelleValeur);
            activity.setTask(task);
            task.getActivities().add(activity);
        }
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