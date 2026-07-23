---
name: sonar-fix
description: Verify commands, Jira templates, and Sonar URL for sonar-fix agent.
---

# Sonar-Fix Reference

## Verify commands (run after every fix)
```bash
[ -f ./mvnw ] && ./mvnw -q -DskipTests compile || mvn -q -DskipTests compile
[ -f ./mvnw ] && ./mvnw -q test || mvn -q test
```
Both must exit 0 = PASS. Non-zero = FAIL, revert the fix.

## Sonar issue URL (must never be blank)
If the MCP returns a URL, use it. Otherwise construct it:
- SonarCloud: `https://sonarcloud.io/project/issues?id=<PROJECT_KEY>&open=<ISSUE_KEY>`
- Self-hosted: `<SONAR_BASE_URL>/project/issues?id=<PROJECT_KEY>&open=<ISSUE_KEY>`

## Jira tickets (French default, one per issue)

Add tickets to the **current active sprint** by default. If the user says "backlog",
leave them in the backlog instead.

### Fixed
Create the ticket, then transition it to a **review/test** status — try "À tester", "In
Review", "En revue", or "Ready for QA" (whichever exists in the project's workflow). If
none exist, use "En cours" / "In Progress". Never leave it in "To Do" — the fix is done,
it needs human review.
```
Résumé : [Sonar] <règle> corrigé dans <Fichier>
Priorité : <MAJOR=Moyenne, MINOR=Basse>   Étiquettes : sonar-fix, ia
Description :
  Problème : <ce que Sonar signale>
  Emplacement : <chemin>:<ligne>
  Correction : <ce qui a changé>
  Lien Sonar : <url>
```

### Escalated (status: To Do)
```
Résumé : [Sonar] À revoir : <règle> dans <Fichier> (<sévérité>)
Priorité : Haute   Étiquettes : sonar-fix, à-traiter
Description :
  Emplacement : <chemin>:<ligne>
  Raison : <pourquoi non corrigé>
  Lien Sonar : <url>
```

### Failed (status: To Do)
```
Résumé : [Sonar] Échec : <règle> dans <Fichier>
Priorité : Moyenne   Étiquettes : sonar-fix, échec
Description :
  Emplacement : <chemin>:<ligne>
  Échec : <erreur, tronquée>
  Lien Sonar : <url>
  État : Annulé — code inchangé.
```
