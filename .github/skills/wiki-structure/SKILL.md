---
name: wiki-structure
description: Documentation standard for /wiki generation/update, .doc-map maintenance, drift reconciliation, Confluence plans, and Jira review drafts.
---

# Wiki Structure

Use for all `/wiki` documentation work.

## Language

Default language is French.
Use French for wiki prose, headings, output block field labels, Confluence content, Jira ticket text, MR descriptions, release summaries, and user-facing status messages.
Use another language only when `AGENTS.md` defines `language:` or the user explicitly requests another language.
Keep code, identifiers, commands, paths, API names, env keys, branch names, approval phrases, action symbols, and block titles unchanged.

## Scope

`/wiki` markdown is the documentation source of truth. `/wiki/.doc-map.json` is an identity/sync index only.
Do not add generated footers, version comments, ownership comments, or timestamps. Preserve existing metadata comments unless editing that exact line is explicitly required.
Do not rely on git history, commit messages, branch names, or issue trackers to prove current behavior.
Ignore generated/vendor/build artifacts, lockfiles, minified files, `node_modules/`, `target/`, `build/`, `dist/`, `.next/`, `.gradle/`, `.idea/`, and coverage output.

## Documentation Map

Maintain `/wiki/.doc-map.json` when `/wiki` changes or Confluence publication status changes.

Minimum schema:

```json
{
  "schemaVersion": 1,
  "project": {
    "name": "<project-or-repository>",
    "language": "fr",
    "platform": "<web|mobile|backend|library|unknown>",
    "tier": 2
  },
  "pages": {
    "wiki/page.md": {
      "title": "<page title>",
      "status": "active",
      "linksTo": [],
      "linkedFrom": [],
      "confluence": {
        "space": "<space>",
        "parentId": "<parentId>",
        "pageId": "<pageId>",
        "title": "<title>",
        "status": "not requested"
      }
    }
  }
}
```

Rules:
- Create or update the map when writes are allowed and `/wiki` changes.
- Recompute titles and links from current markdown files.
- Use map `pageId` to resolve Confluence identity when available.
- After Confluence publication, update pageId/title/status for affected pages.
- Mark a deleted local wiki file as `deleted` only when the user explicitly requested deletion.
- Do not include code behavior claims in Phase 1.
- Do not use stale map entries to override current markdown, current code, or safe Confluence matching.

## Platform and Tier

Declare one dominant platform per wiki domain: web frontend, mobile, backend/service, or library/SDK.
Use deterministic repo signals. Reuse cached `.doc-map.json` tier only if current signals still fit.
Reclassify only when missing, user requested, or repo/wiki signals clearly changed.
Tier affects depth only. Do not invent pages, ADRs, versions, owners, or migration timelines to satisfy a tier.

## Drift Reconciliation

Verify existing wiki claims against current code: classes/functions, endpoints, schemas, env keys, CLI commands, screens/routes/states, dependencies, auth/integrations, documented behavior.

Actions:
- `📝 UPDATE`: current code differs from documented claim.
- `➕ ADD`: meaningful current code surface is undocumented.
- `⚠️ DEPRECATE`: documented behavior is removed from current code.
- `📝 RELINK`: indexes, navigation, or cross-links are broken/stale.
- `🗑️ DELETE`: only when the user explicitly requested local `/wiki` deletion.
- `📝 MAP`: `.doc-map.json` changed.
- `📝 ADR`: only when the user explicitly requested an ADR or an existing documented decision is being normalized.

Only flag confirmed drift. If uncertain, leave unchanged or mark unresolved outside Jira.
Prioritize onboarding/setup/config, user-facing behavior, APIs/contracts/events, operations/runbooks/release flow, integrations/auth/security-relevant behavior, and necessary architecture. Avoid trivial internals.

## Edit Rules

### UPDATE
Edit only affected sections. Preserve surrounding content, heading depth, voice, examples, and formatting. No opportunistic cleanup.

### ADD
Use the standard page template. Match naming convention. Link new pages from one relevant page and one index/navigation page when possible. No stubs.

### DEPRECATE
Insert immediately after `# Title`:

```markdown
> ⚠️ **Deprecated:** This documented behavior is no longer present in the current codebase.
> Use <replacement/link> when a confirmed replacement exists.
```

Preserve historical content below. Do not add fake versions, dates, or timelines.

### DELETE
Only for explicit user requests. Delete local `/wiki` files only after verifying path is inside `/wiki`. Remove/update links. Mark local deletion separately from Confluence deletion/archive.

### ADR
ADR creation is disabled by default. Never invent deciders, dates, rationale, alternatives, or consequences. If no documented decision exists, update an architecture page instead.

## Output Blocks

### WIKI MAP

```text
WIKI MAP
────────────────────────────────────────────────────────
Plateforme détectée :     <web|mobile|backend|library> (<details>)
Tier détecté :            <1|2|3>
Source du tier :          <détecté|confirmé par .doc-map|modifié depuis .doc-map>
Pages totales :           <N>
Doc map :                 <créée|mise à jour|inchangée|non écrite>
Convention de nommage :   <kebab-case|Title Case|autre>
Ton observé :             <style>
Style de callout :        <style>
Style des blocs de code : <style>
Sections de l’accueil :   <list>
Pages orphelines :        <list|Aucune>
────────────────────────────────────────────────────────
```

### EDITORIAL PLAN

```text
EDITORIAL PLAN — <Project Name or repository>
══════════════════════════════════════════════════════════════
Plateforme : <platform> | Tier : <1|2|3> | Pages affectées : <N> | Nouvelles : <N>

📝 UPDATE     wiki/path/page.md
              <divergence confirmée exacte>
➕ ADD        wiki/path/new-page.md
              <pourquoi nécessaire; liens entrants/index>
⚠️ DEPRECATE  wiki/path/old.md
              <comportement supprimé; remplacement confirmé ou aucun>
📝 RELINK     wiki/Home.md
              <changements navigation/index>
🗑️ DELETE     wiki/path/file.md
              <demande explicite; suppression locale uniquement>
📝 MAP        wiki/.doc-map.json
              <créée/mise à jour car pages, liens, mappings ou statut changés>
📝 ADR        wiki/decisions/adr-NNN-title.md
              <uniquement décision explicitement demandée/confirmée>
══════════════════════════════════════════════════════════════
```

Do not include unconfirmed ideas or recommendations.

### CONFLUENCE PUBLICATION PLAN

```text
CONFLUENCE PUBLICATION PLAN
────────────────────────────────────────────────────────
Espace :               <space | bloqué : manquant>
Parent :               <parent | bloqué : manquant>
Statut d’approbation : en attente d’approbation
Source d’identité :    <pageId .doc-map | titre exact | bloqué>

UPDATE
- wiki/path/page.md → "<Confluence title>" [pageId: <id>]

CREATE
- wiki/path/new-page.md → "<Confluence title>"

SKIP
- wiki/path/page.md — <raison>

NEEDS MANUAL RESOLUTION
- wiki/path/page.md — <raison duplicate/ambiguë/mapping manquant>

REQUIRES DELETION APPROVAL
- "<Confluence title>" [pageId: <id>] — fichier local supprimé
────────────────────────────────────────────────────────
Approbation requise : répondre `approve confluence` pour publier.
```

Confluence rules:
- Publish only after approval and only pages listed in the latest approved plan.
- Resolve by configured space, configured parent, `.doc-map.json` pageId if available, then exact normalized title.
- Update only when exactly one safe target exists.
- Create only when no same-title page exists under the target parent.
- If duplicate, ambiguous, unmapped, stale map, or missing config: skip or mark manual resolution.
- Never perform broad cleanup.
- Never delete, archive, or move without exact approval naming title or page ID.
- After successful publication, update `.doc-map.json` mapping/status.

### JIRA TICKET DRAFT

```text
JIRA TICKET DRAFT
────────────────────────────────────────────────────────
Type de ticket : Task
Résumé : Revue documentation : <Project Name or repository>

Description :
MR : <lien MR ou aucune MR>
Confluence : <lien | en attente d’approbation | non demandé | bloqué : raison>

Pages :
- Mises à jour : <N>
- Ajoutées : <N>
- Dépréciées : <N>
- Supprimées : <N>
────────────────────────────────────────────────────────
Approbation requise : répondre `approve jira` pour créer le ticket.
```

Jira hard cap: allowed only issue type, summary, and description containing MR link, Confluence status/link, and page counts. Field labels must be French by default. Forbidden: labels unless explicitly requested, checklist, MCP diagnostics, workflow logs, skipped-stage details, recommendations, failure logs, speculation, implementation notes not in MR, and requests to synchronize `/wiki` and Confluence.

### RELEASE DOCUMENTATION SUMMARY

```text
RELEASE DOCUMENTATION SUMMARY
────────────────────────────────────────────────────────
Pages mises à jour :      <N>
Pages ajoutées :          <N>
Pages dépréciées :        <N>
Pages supprimées :        <N>
Doc map :                 <créée|mise à jour|inchangée|non écrite>
Liens vérifiés :          <✓|partiel|non exécuté>
Index mis à jour :        <✓|partiel|non nécessaire>
Breaking changes :        <Aucun|liste>
MR GitLab :               <lien|aucune MR|échec|non demandé>
Confluence :              <non demandé|en attente d’approbation|bloqué : raison|publié|partiel|échec>
Jira :                    <non demandé|en attente d’approbation|bloqué : raison|créé|échec>
Approbation en attente :  <Aucune|approve confluence|approve jira|approve all>
────────────────────────────────────────────────────────
```

## Standard Page Template

```markdown
# Page Title

_One sentence describing what this page covers and who it is for._

## Overview

Explain what this is, why it matters, and when to use it.

## <Primary Section>

Document confirmed behavior with concise examples when useful.

## Related

- ./related.md — why it matters
```

## Writing Standards

- Active voice. Present tense. Imperative for instructions.
- One idea per paragraph.
- No filler, TODO, stub, “coming soon”, or “see source code”.
- Define acronyms on first use per page.
- Use tables for comparisons.
- Use realistic, runnable commands with prerequisites immediately before commands.
- Language-tag all code blocks.
- Keep internal links relative inside `/wiki`.

Callouts:

```markdown
> ℹ️ **Note:** Important context.
> ⚠️ **Warning:** Risky behavior.
> ⚠️ **Breaking Change:** Integration-breaking change.
```
