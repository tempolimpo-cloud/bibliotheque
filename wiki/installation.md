# Installation

_Guide d'installation et de configuration pour démarrer l'application Bibliotheque en local._

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java 17** ou supérieur
- **Maven 3.6+** pour la gestion des dépendances
- Un IDE Java (IntelliJ IDEA, Eclipse, VS Code recommandés)

## Installation

### Cloner le projet

```bash
git clone <url-du-repository>
cd agent/bibliotheque
```

### Installer les dépendances

```bash
mvn clean install
```

Cette commande télécharge toutes les dépendances définies dans le `pom.xml` et compile le projet.

## Démarrage de l'application

### Démarrage en mode développement

```bash
mvn spring-boot:run
```

L'application démarre sur le port **8080** par défaut.

### Vérifier le démarrage

Accédez à l'API via :

```bash
curl http://localhost:8080/api/livres/recherche/auteur?auteur=test
```

Une réponse JSON vide `[]` confirme que l'API fonctionne correctement.

## Configuration

### Base de données H2

L'application utilise une base de données H2 en mémoire. Les données sont perdues à chaque redémarrage.

La console H2 peut être activée en ajoutant dans `src/main/resources/application.properties` :

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Accès à la console : `http://localhost:8080/h2-console`

Paramètres de connexion :
- **JDBC URL** : `jdbc:h2:mem:testdb`
- **User** : `sa`
- **Password** : _(laisser vide)_

### Port de l'application

Pour modifier le port par défaut, ajoutez dans `application.properties` :

```properties
server.port=9090
```

## Build pour la production

### Créer un JAR exécutable

```bash
mvn clean package
```

Le fichier JAR est généré dans `target/bibliotheque-1.0.0.jar`.

### Exécuter le JAR

```bash
java -jar target/bibliotheque-1.0.0.jar
```

## Tests

### Exécuter les tests unitaires

```bash
mvn test
```

> ℹ️ **Note:** Les tests utilisent une base H2 dédiée, isolée de l'environnement de développement.

## Dépendances principales

| Dépendance | Version | Usage |
|------------|---------|-------|
| spring-boot-starter-web | 3.2.5 | API REST |
| spring-boot-starter-data-jpa | 3.2.5 | Persistance JPA |
| h2 | runtime | Base de données en mémoire |
| spring-boot-starter-validation | 3.2.5 | Validation des DTOs |
| lombok | optional | Réduction du code boilerplate |

## Related

- [./Home.md](./Home.md) — Vue d'ensemble du projet
- [./api-reference.md](./api-reference.md) — Documentation des endpoints
