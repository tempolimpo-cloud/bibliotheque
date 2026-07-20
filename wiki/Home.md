# Bibliotheque API

_Documentation de l'API REST pour la gestion des livres d'une bibliothèque._

## Vue d'ensemble

Bibliotheque est une API REST développée avec Spring Boot 3.2.5 et Java 17, permettant de gérer un catalogue de livres et leurs emprunts. L'API offre des fonctionnalités de recherche, d'emprunt et de retour de livres.

## Fonctionnalités principales

- **Recherche de livres** : par auteur ou catégorie
- **Gestion des emprunts** : emprunter et retourner des livres
- **Validation des données** : contraintes sur ISBN et champs obligatoires
- **Gestion des erreurs** : réponses HTTP structurées pour les cas d'erreur

## Stack technique

- **Framework** : Spring Boot 3.2.5
- **Langage** : Java 17
- **Base de données** : H2 (en mémoire)
- **Persistance** : Spring Data JPA
- **Validation** : Jakarta Bean Validation
- **Build** : Maven

## Structure de la documentation

- [Installation](./installation.md) — Configuration et démarrage du projet
- [Référence API](./api-reference.md) — Documentation complète des endpoints REST
- [Architecture](./architecture.md) — Structure du code et composants techniques

## Démarrage rapide

```bash
cd bibliotheque
mvn spring-boot:run
```

L'API est accessible sur `http://localhost:8080/api/livres`.

## Modèle de données

Chaque livre contient :
- `id` : identifiant unique (Long)
- `titre` : titre du livre (obligatoire)
- `auteur` : auteur du livre (obligatoire)
- `isbn` : code ISBN unique à 10 ou 13 chiffres (obligatoire)
- `categorie` : catégorie du livre (optionnel)
- `anneePublication` : année de publication (obligatoire)
- `disponible` : statut de disponibilité (booléen)

## Related

- [./installation.md](./installation.md) — Guide d'installation complet
- [./api-reference.md](./api-reference.md) — Endpoints et exemples d'utilisation
