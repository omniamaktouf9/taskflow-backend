package com.taskflow.taskflow_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    private String description;

    private String statut;

    private String priorite;

    private String dateEcheance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}