package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/reminders")
    public String testerRappels() {
        reminderService.verifierEcheancesDemain();
        return "Vérification des échéances effectuée.";
    }
}