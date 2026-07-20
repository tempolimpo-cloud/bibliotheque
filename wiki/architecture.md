# Architecture

_Description de l'architecture technique et des composants de l'application Bibliotheque._

## Vue d'ensemble

L'application suit une architecture en couches typique de Spring Boot, séparant les responsabilités entre présentation, métier et persistance.

## Structure des couches

```
┌─────────────────────────────────────────┐
│         LivreController                 │  Couche présentation
│        (REST endpoints)                 │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      LivreService / LivreServiceImpl    │  Couche métier
│     (logique métier et validation)      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         LivreRepository                 │  Couche persistance
│        (accès base de données)          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Base de données H2              │
└─────────────────────────────────────────┘
```

## Composants principaux

### Couche présentation

#### LivreController

Contrôleur REST exposant 4 endpoints publics :

```java
@RestController
@RequestMapping("/api/livres")
public class LivreController
```

**Endpoints exposés** :
- `GET /api/livres/recherche/auteur` : recherche par auteur
- `GET /api/livres/recherche/categorie` : recherche par catégorie
- `PATCH /api/livres/{id}/emprunter` : emprunter un livre
- `PATCH /api/livres/{id}/retourner` : retourner un livre

> ℹ️ **Note:** Les méthodes CRUD de base (ajouter, lire, modifier, supprimer) sont implémentées dans le service mais non exposées dans le controller.

### Couche métier

#### LivreService

Interface définissant le contrat de service avec 11 méthodes :

- Gestion des livres : `ajouterLivre`, `obtenirLivre`, `obtenirTousLesLivres`, `modifierLivre`, `supprimerLivre`
- Recherche : `rechercherParTitre`, `rechercherParAuteur`, `rechercherParCategorie`
- Disponibilité : `obtenirLivresDisponibles`
- Emprunts : `emprunterLivre`, `retournerLivre`

#### LivreServiceImpl

Implémentation du service avec :
- Annotation `@Transactional` pour la gestion transactionnelle
- Validation métier (unicité ISBN, disponibilité)
- Conversion entre entités et DTOs
- Gestion des exceptions métier

**Validations métier** :
- ISBN unique lors de l'ajout ou modification
- Livre disponible avant emprunt
- Livre emprunté avant retour

### Couche persistance

#### LivreRepository

Repository JPA étendant `JpaRepository<Livre, Long>` avec des requêtes personnalisées :

```java
List<Livre> findByDisponibleTrue();
List<Livre> findByAuteurContainingIgnoreCase(String auteur);
List<Livre> findByTitreContainingIgnoreCase(String titre);
List<Livre> findByCategorieIgnoreCase(String categorie);
Optional<Livre> findByIsbn(String isbn);
boolean existsByIsbn(String isbn);
```

#### Livre

Entité JPA mappée sur la table `livres` :

```java
@Entity
@Table(name = "livres")
public class Livre
```

**Contraintes** :
- `id` : clé primaire auto-incrémentée
- `isbn` : unique et non null
- `titre` et `auteur` : non null
- `disponible` : valeur par défaut `true`

### DTOs

#### LivreRequest

DTO pour les requêtes entrantes avec validation Jakarta Bean Validation :

- `@NotBlank` sur titre, auteur, ISBN
- `@Pattern` sur ISBN : 10 ou 13 chiffres
- `@NotNull` sur anneePublication

#### LivreResponse

DTO pour les réponses sortantes, contenant tous les champs de l'entité Livre.

### Gestion des erreurs

#### GlobalExceptionHandler

Classe `@RestControllerAdvice` interceptant les exceptions et retournant des réponses HTTP structurées :

**Exceptions gérées** :
- `LivreNotFoundException` → 404 NOT FOUND
- `IsbnDejaExistantException` → 409 CONFLICT
- `MethodArgumentNotValidException` → 400 BAD REQUEST
- `IllegalStateException` → 500 INTERNAL SERVER ERROR (emprunts/retours)

**Structure des réponses d'erreur** :

```json
{
  "timestamp": "2026-07-20T10:30:00",
  "statut": 404,
  "message": "Livre avec l'ID 1 introuvable."
}
```

Pour les erreurs de validation, un objet `erreurs` contient les détails par champ.

## Technologies utilisées

| Technologie | Version | Usage |
|-------------|---------|-------|
| Spring Boot | 3.2.5 | Framework applicatif |
| Java | 17 | Langage de programmation |
| Spring Data JPA | 3.2.5 | Abstraction ORM |
| H2 Database | runtime | Base de données en mémoire |
| Jakarta Validation | 3.2.5 | Validation des beans |
| Lombok | optional | Réduction code boilerplate |
| Maven | - | Gestion des dépendances |

## Configuration

### Base de données

Configuration par défaut de H2 :
- Mode en mémoire (les données sont perdues au redémarrage)
- URL JDBC : `jdbc:h2:mem:testdb`
- Utilisateur : `sa`
- Pas de mot de passe

### Dépendances Maven

Toutes les dépendances sont définies dans `pom.xml` avec le parent `spring-boot-starter-parent:3.2.5`.

## Points d'extension

Pour étendre l'application :

1. **Exposer les endpoints CRUD** : ajouter les mappings dans `LivreController` pour `POST`, `GET`, `PUT`, `DELETE`
2. **Ajouter l'authentification** : intégrer Spring Security
3. **Persistance durable** : remplacer H2 par PostgreSQL ou MySQL
4. **Pagination** : utiliser `Pageable` dans les requêtes de recherche
5. **API documentation** : intégrer Swagger/OpenAPI

## Related

- [./Home.md](./Home.md) — Vue d'ensemble du projet
- [./api-reference.md](./api-reference.md) — Documentation des endpoints
- [./installation.md](./installation.md) — Installation et configuration
