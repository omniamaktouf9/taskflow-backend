package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    // Tous les jours à 8h00 du matin
    @Scheduled(cron = "0 0 8 * * *")
    public void verifierEcheancesDemain() {
        LocalDate demain = LocalDate.now().plusDays(1);
        String dateEcheanceDemain = demain.format(DateTimeFormatter.ISO_LOCAL_DATE);

        List<Task> tachesAVenir = taskRepository.findByDateEcheanceAndStatutNot(dateEcheanceDemain, "TERMINE");

        for (Task task : tachesAVenir) {
            envoyerRappel(task);
        }
    }

    public void envoyerRappel(Task task) {
        if (task.getUser() == null || task.getUser().getEmail() == null) {
            return;
        }

        String nomProjet = task.getProject() != null ? task.getProject().getNom() : "Sans projet";

        String sujet = "Rappel : échéance demain pour \"" + task.getTitre() + "\"";
        String contenu = "Bonjour " + task.getUser().getNom() + ",\n\n"
                + "Votre tâche \"" + task.getTitre() + "\" (projet : " + nomProjet + ") "
                + "arrive à échéance demain (" + task.getDateEcheance() + ").\n\n"
                + "Connectez-vous à TaskFlow pour la consulter.\n\n"
                + "— L'équipe TaskFlow";

        emailService.envoyerEmail(task.getUser().getEmail(), sujet, contenu);
    }
}