# Un projet pédagogique dans le cadre de la formation "Développeur Full-Stack - Java et Angular" (FR)

## Présentation
Ce projet est l’API back-end d’un portail de location permettant la mise en relation entre propriétaires et locataires.

À l’origine, l’application Angular (version 14) fonctionnait avec des données mockées via Mockoon. Ce backend Spring Boot remplace désormais l’ensemble des routes mockées.

Le projet inclut :

- Authentification avec JWT
- CRUD des locations
- Upload et stockage des images
- Sécurisation complète des routes
- Documentation Swagger
- Base MySQL
- Architecture en couches (Controller / Service / Repository)

## Fonctionnalités

### Authentification

- Création d’un compte
- Connexion → renvoie un JWT
- Routes sécurisées (sauf register/login)
- Mots de passe hashés avec BCrypt

### Gestion des Locations

- Création / modification / liste des locations
- Upload d’image (multipart/form-data)
- Stockage dans /uploads
- URL publique enregistrée en base (picture)

### Documentation API

Accessible via : http://localhost:8080/api/swagger-ui/index.html

### Base de données

- MySQL
- Entités JPA
- Champs automatiques created_at, updated_at

## Structure du Projet
```bash
src/
 └── main/
     ├── java/
     │     ├── com.opemclassrooms.api/
     │     ├───── com.opemclassrooms.api.configuration/
     │     ├── com.opemclassrooms.api.controller/
     │     ├── com.opemclassrooms.api.repository/
     │     ├── com.opemclassrooms.api.service/
     │     ├── com.opemclassrooms.api.model/
     │     ├── com.opemclassrooms.api.dto/
     │     └── com.opemclassrooms.api.exception/
     └── resources/
     │     └── application.properties
     │
     └── uploads/
```

## Installation et lancement

1. Cloner le dépôt
2. Créer la base MySql
3. Configurer le projet :
- Créer dans le racine du projet un fichier .env
- Ajoutez vos informations de connexion et votre clé secrète JWT :
```bash
    DB_URL=localhost:3306/db
    DB_USERNAME=root
    DB_PASSWORD=password
    JWT_SECRET=secret_word
```
4. Lancer l’API avec l'IDE Eclipse ou dans un console.

# An educational project as part of the "Full-Stack Developer – Java & Angular" training program (EN)

## Overview
This project is the back-end API of a rental portal designed to connect property owners with tenants.

Originally, the Angular application (version 14) worked with mocked data using Mockoon.
This Spring Boot backend now replaces all mocked routes with real API endpoints.

The project includes:

- JWT authentication
- Rental CRUD operations
- Image upload and storage
- Full route security
- Swagger documentation
- MySQL database
- Layered architecture (Controller / Service / Repository)

## Features

### Authentication

- User account creation
- Login → returns a JWT
- Secured routes (except register/login)
- Passwords hashed with BCrypt

### Rental Management

- Create / update / list rentals
- Image upload (multipart/form-data)
- File storage under /uploads
- Public image URL stored in the database (picture field)

### API Documentation

Available at: http://localhost:8080/api/swagger-ui/index.html

### Database

- MySQL
- JPA entities
- Automatic timestamp fields (created_at, updated_at)

## Project Structure
```bash
src/
 └── main/
     ├── java/
     │     ├── com.opemclassrooms.api/
     │     ├───── com.opemclassrooms.api.configuration/
     │     ├── com.opemclassrooms.api.controller/
     │     ├── com.opemclassrooms.api.repository/
     │     ├── com.opemclassrooms.api.service/
     │     ├── com.opemclassrooms.api.model/
     │     ├── com.opemclassrooms.api.dto/
     │     └── com.opemclassrooms.api.exception/
     └── resources/
     │     └── application.properties
     │
     └── uploads/
```

## Installation and Run Instructions

1. Clone the repository
2. Create the MySQL database
3. Configure the project:
- Create a .env file at the project root
- Add your database credentials and JWT secret key:
```bash
    DB_URL=localhost:3306/db
    DB_USERNAME=root
    DB_PASSWORD=password
    JWT_SECRET=secret_word
```
4. Run the API from Eclipse or via the command line.