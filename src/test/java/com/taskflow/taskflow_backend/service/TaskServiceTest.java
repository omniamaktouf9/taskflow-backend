package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.model.User;
import com.taskflow.taskflow_backend.model.Tag;
import com.taskflow.taskflow_backend.repository.TagRepository;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void uneTacheSansDependancesNEstPasBloquee() {
        Task task = new Task();
        task.setDependencies(new HashSet<>());

        boolean resultat = taskService.estBloquee(task);

        assertFalse(resultat);
    }

    @Test
    void uneTacheAvecDependanceTermineeNEstPasBloquee() {
        Task dependance = new Task();
        dependance.setStatut("TERMINE");

        Set<Task> dependances = new HashSet<>();
        dependances.add(dependance);

        Task task = new Task();
        task.setDependencies(dependances);

        boolean resultat = taskService.estBloquee(task);

        assertFalse(resultat);
    }

    @Test
    void uneTacheAvecDependanceNonTermineeEstBloquee() {
        Task dependance = new Task();
        dependance.setStatut("EN_COURS");

        Set<Task> dependances = new HashSet<>();
        dependances.add(dependance);

        Task task = new Task();
        task.setDependencies(dependances);

        boolean resultat = taskService.estBloquee(task);

        assertTrue(resultat);
    }

    @Test
    void creerUneTacheAssocieBienLUtilisateurConnecte() {
        User user = new User();
        user.setEmail("omnia@test.com");
        user.setNom("Omnia");

        when(userRepository.findByEmail("omnia@test.com")).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task nouvelleTask = new Task();
        nouvelleTask.setTitre("Nouvelle tâche");

        Task resultat = taskService.createTask(nouvelleTask, "omnia@test.com");

        assertNotNull(resultat.getUser());
        assertEquals("omnia@test.com", resultat.getUser().getEmail());
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void creerUneTacheReutiliseUnTagExistant() {
        User user = new User();
        user.setEmail("omnia@test.com");

        Tag tagExistant = new Tag();
        tagExistant.setId(1L);
        tagExistant.setNom("urgent");

        when(userRepository.findByEmail("omnia@test.com")).thenReturn(Optional.of(user));
        when(tagRepository.findByNom("urgent")).thenReturn(Optional.of(tagExistant));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tag tagEnvoye = new Tag();
        tagEnvoye.setNom("urgent");
        Set<Tag> tags = new HashSet<>();
        tags.add(tagEnvoye);

        Task nouvelleTask = new Task();
        nouvelleTask.setTitre("Tâche avec tag");
        nouvelleTask.setTags(tags);

        Task resultat = taskService.createTask(nouvelleTask, "omnia@test.com");

        assertEquals(1, resultat.getTags().size());
        assertEquals(1L, resultat.getTags().iterator().next().getId());
        verify(tagRepository, never()).save(any(Tag.class));
    }
}