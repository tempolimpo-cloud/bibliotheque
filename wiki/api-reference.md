# Référence API

_Documentation complète des endpoints REST de l'API Bibliotheque._

## Base URL

```
http://localhost:8080/api/livres
```

## Endpoints

### Recherche par auteur

Recherche les livres par nom d'auteur (insensible à la casse, recherche partielle).

**Endpoint**

```http
GET /api/livres/recherche/auteur?auteur={auteur}
```

**Paramètres**

| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `auteur` | String | Oui | Nom ou partie du nom de l'auteur |

**Réponse succès** : `200 OK`

```json
[
  {
    "id": 1,
    "titre": "Le Petit Prince",
    "auteur": "Antoine de Saint-Exupéry",
    "isbn": "9782070612758",
    "categorie": "Littérature",
    "anneePublication": 1943,
    "disponible": true
  }
]
```

**Exemple**

```bash
curl "http://localhost:8080/api/livres/recherche/auteur?auteur=Saint"
```

---

### Recherche par catégorie

Recherche les livres appartenant à une catégorie (insensible à la casse, correspondance exacte).

**Endpoint**

```http
GET /api/livres/recherche/categorie?categorie={categorie}
```

**Paramètres**

| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `categorie` | String | Oui | Nom exact de la catégorie |

**Réponse succès** : `200 OK`

```json
[
  {
    "id": 2,
    "titre": "1984",
    "auteur": "George Orwell",
    "isbn": "9780451524935",
    "categorie": "Science-Fiction",
    "anneePublication": 1949,
    "disponible": false
  }
]
```

**Exemple**

```bash
curl "http://localhost:8080/api/livres/recherche/categorie?categorie=Science-Fiction"
```

---

### Emprunter un livre

Marque un livre comme emprunté (passe `disponible` à `false`).

**Endpoint**

```http
PATCH /api/livres/{id}/emprunter
```

**Paramètres**

| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `id` | Long | Oui | Identifiant du livre (path parameter) |

**Réponse succès** : `200 OK`

```json
{
  "id": 1,
  "titre": "Le Petit Prince",
  "auteur": "Antoine de Saint-Exupéry",
  "isbn": "9782070612758",
  "categorie": "Littérature",
  "anneePublication": 1943,
  "disponible": false
}
```

**Erreurs**

| Code | Description |
|------|-------------|
| `404 NOT FOUND` | Livre avec cet `id` introuvable |
| `500 INTERNAL SERVER ERROR` | Le livre n'est pas disponible (déjà emprunté) |

**Exemple**

```bash
curl -X PATCH http://localhost:8080/api/livres/1/emprunter
```

> ⚠️ **Warning:** Si le livre n'est pas disponible, l'API retourne une `IllegalStateException` avec le message "Le livre \"{titre}\" n'est pas disponible."

---

### Retourner un livre

Marque un livre comme retourné (passe `disponible` à `true`).

**Endpoint**

```http
PATCH /api/livres/{id}/retourner
```

**Paramètres**

| Paramètre | Type | Obligatoire | Description |
|-----------|------|-------------|-------------|
| `id` | Long | Oui | Identifiant du livre (path parameter) |

**Réponse succès** : `200 OK`

```json
{
  "id": 1,
  "titre": "Le Petit Prince",
  "auteur": "Antoine de Saint-Exupéry",
  "isbn": "9782070612758",
  "categorie": "Littérature",
  "anneePublication": 1943,
  "disponible": true
}
```

**Erreurs**

| Code | Description |
|------|-------------|
| `404 NOT FOUND` | Livre avec cet `id` introuvable |
| `500 INTERNAL SERVER ERROR` | Le livre est déjà disponible |

**Exemple**

```bash
curl -X PATCH http://localhost:8080/api/livres/1/retourner
```

> ⚠️ **Warning:** Si le livre est déjà disponible, l'API retourne une `IllegalStateException` avec le message "Le livre \"{titre}\" est déjà disponible."

---

## Gestion des erreurs

Toutes les réponses d'erreur suivent une structure JSON standardisée.

### Erreur 404 - Livre non trouvé

```json
{
  "timestamp": "2026-07-20T10:30:00",
  "statut": 404,
  "message": "Livre avec l'ID 999 introuvable."
}
```

### Erreur 409 - ISBN existant

```json
{
  "timestamp": "2026-07-20T10:30:00",
  "statut": 409,
  "message": "L'ISBN 9782070612758 existe déjà."
}
```

### Erreur 400 - Validation échouée

```json
{
  "timestamp": "2026-07-20T10:30:00",
  "statut": 400,
  "erreurs": {
    "titre": "Le titre est obligatoire",
    "isbn": "ISBN invalide (10 ou 13 chiffres)"
  }
}
```

## Modèle de données

### LivreResponse

Structure retournée par tous les endpoints.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | Long | Identifiant unique du livre |
| `titre` | String | Titre du livre |
| `auteur` | String | Auteur du livre |
| `isbn` | String | Code ISBN (10 ou 13 chiffres) |
| `categorie` | String | Catégorie du livre (peut être null) |
| `anneePublication` | Integer | Année de publication |
| `disponible` | boolean | Indique si le livre est disponible à l'emprunt |

### LivreRequest

Structure attendue pour créer ou modifier un livre (non exposée publiquement dans le controller actuel).

| Champ | Type | Obligatoire | Validation |
|-------|------|-------------|------------|
| `titre` | String | Oui | Non vide |
| `auteur` | String | Oui | Non vide |
| `isbn` | String | Oui | Format : 10 ou 13 chiffres |
| `categorie` | String | Non | - |
| `anneePublication` | Integer | Oui | Non null |

> ℹ️ **Note:** Les endpoints CRUD de base (ajout, modification, suppression) sont implémentés dans le service `LivreService` mais ne sont pas exposés dans le controller actuel.

## Related

- [./Home.md](./Home.md) — Vue d'ensemble du projet
- [./architecture.md](./architecture.md) — Architecture et composants
