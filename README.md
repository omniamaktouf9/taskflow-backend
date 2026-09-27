# TaskFlow - Backend

API REST pour une application de gestion de tâches, avec authentification JWT et isolation des données par utilisateur.

## Technologies utilisées

- **Java 17**
- **Spring Boot 4.1.1**
- **Spring Security** (authentification JWT)
- **Spring Data JPA** (accès base de données)
- **PostgreSQL** (base de données)
- **JJWT** (génération et validation de tokens JWT)
- **Maven** (gestion des dépendances)
- **Lombok**

## Fonctionnalités

- Inscription et connexion sécurisées (JWT)
- Chaque utilisateur ne voit et ne modifie que ses propres tâches
- CRUD complet des tâches (créer, lire, modifier, supprimer)
- Mots de passe hashés (BCrypt)
- Protection CORS configurée pour le frontend Angular

## Endpoints API

### Authentification

| Méthode | Endpoint              | Description                          |
|---------|------------------------|---------------------------------------|
| POST    | `/api/auth/register`  | Créer un compte                      |
| POST    | `/api/auth/login`     | Se connecter, retourne un token JWT  |

### Tâches (authentification requise)

| Méthode | Endpoint          | Description                                      |
|---------|-------------------|---------------------------------------------------|
| GET     | `/api/tasks`      | Récupérer les tâches de l'utilisateur connecté    |
| POST    | `/api/tasks`      | Créer une nouvelle tâche                          |
| PUT     | `/api/tasks/{id}` | Modifier une tâche                                |
| DELETE  | `/api/tasks/{id}` | Supprimer une tâche                               |

Toutes les routes `/api/tasks/**` nécessitent un header :

Authorization: Bearer <votre_token_jwt>


## Lancer le projet en local

### Prérequis
- Java 17+
- Maven
- PostgreSQL installé et lancé

### Étapes

1. Créer une base de données PostgreSQL nommée `taskflow_db`

2. Configurer `src/main/resources/application.properties` avec vos identifiants :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskflow_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:votre_mot_de_passe}
```

3. Lancer l'application :
```bash
mvn spring-boot:run
```

L'API sera disponible sur `http://localhost:8080`

## Projet frontend associé

Ce backend fonctionne avec le frontend Angular disponible ici : [taskflow-frontend](https://github.com/omniamaktouf9/taskflow-frontend)