---
name: doc-gen
description: Génère/met à jour /wiki, maintient .doc-map, prépare Confluence/Jira avec approbation, ouvre MR GitLab.
---

# Doc-Gen

Use `wiki-structure`. Act immediately, but respect approval gates.

## Defaults

- Default user-facing language is French for all agent-authored text: chat/status messages, output blocks, wiki prose, Confluence content, Jira text, MR descriptions, and final status line.
- Use `AGENTS.md` `language:` if present; user phrase “in English” overrides for the current run.
- Keep code, identifiers, commands, paths, API names, env keys, branch names, approval phrases, action symbols, and block titles unchanged.
- Translate output block field labels to French.
- `/wiki` absent → bootstrap minimal wiki. `/wiki` present → update confirmed drift only.
- Source of truth for behavior: current codebase, never git history.
- Source of truth for docs content: `/wiki` markdown.
- Edit only `/wiki`, including `/wiki/.doc-map.json` for documentation identity/sync metadata.
- Do not create or edit `AGENTS.md` unless explicitly requested.
- Do not edit source code. Do not output wiki pages in chat. Never merge MR.

## Modes

- `auto` default: update `/wiki`, update `.doc-map.json`, branch, commit, push, open MR; prepare Confluence/Jira drafts only.
- `local-only`: update `/wiki` and `.doc-map.json` only. No Confluence, Jira, or MR unless explicitly requested.
- `review` or `dry-run`: inspect and emit `WIKI MAP` + `EDITORIAL PLAN`; no writes or side effects.
- `scope <path|module|feature>`: restrict analysis and edits to that scope.

## Approval Gates

In `auto`, no additional approval is needed for repository reads, `/wiki` edits, `.doc-map.json` updates, GitLab branch/commit/push/MR.

Explicit approval is required before Confluence create/update/delete/archive/move and Jira ticket creation.

Accepted phrases: `approve confluence`, `approve jira`, `approve all`.
Approval applies only to the latest emitted `CONFLUENCE PUBLICATION PLAN` or `JIRA TICKET DRAFT`.
If files, MR, plan, or draft changed after approval was requested, regenerate the affected plan/draft and request approval again.
If approval is missing, mark action `en attente d’approbation`. Do not treat as failure.

Allowed external statuses in French: `non demandé`, `en attente d’approbation`, `bloqué : <raison>`, `publié`, `partiel`, `échec`, `créé`.

## Documentation Map

If `/wiki/.doc-map.json` exists, use it as a documentation identity index. If it does not exist, create it when `/wiki` is generated or updated.

The map may track wiki paths, titles, active/deprecated/deleted status, links/backlinks, Confluence space/parentId/pageId/title/status, and cached platform/tier.

The map is an index/cache, not source of truth. Never use stale map entries to override current code or current wiki content.
Update the map when wiki pages, links, Confluence mappings, publication status, or explicit local deletion status change.

## Hard Rules

- Do not invent versions, owners, teams, dates, deciders, Jira project keys, Confluence spaces, or parents.
- If required config is missing, mark blocked/awaiting config in French.
- Warm up needed MCPs; retry failed warm-up once; report remaining failures only when they affect a requested action.
- Never put MCP failures, workflow logs, recommendations, or sync instructions in Jira.
- Confluence mirrors `/wiki`; it is not the source of truth.
- Do not perform broad Confluence cleanup.
- Do not create duplicate Confluence pages.
- Do not create ADRs unless user explicitly asks or an existing documented decision is being normalized.

## Workflow

1. Initialize: warm MCPs, read `AGENTS.md` if present, detect mode/language/project.
2. Read or create `/wiki/.doc-map.json` when writes are allowed.
3. Use `wiki-structure` to detect or confirm platform/tier/style. Emit `WIKI MAP` with French field labels.
4. Reconcile `/wiki` ↔ current code: `UPDATE`, `ADD`, `DEPRECATE`, `RELINK`; delete only on explicit user request.
5. Emit `EDITORIAL PLAN` with confirmed actions only and French field labels.
6. Write/update markdown inside `/wiki`; preserve style. Do not add generated footers or version comments. Preserve existing ones unless editing that exact line is explicitly required.
7. Update `/wiki/.doc-map.json` from current wiki files and known Confluence mappings.
8. If user explicitly requested local file deletion: verify path is inside `/wiki`, delete it, relink indexes, update map, and report local deletion separately from Confluence deletion.
9. Prepare exact `CONFLUENCE PUBLICATION PLAN` unless mode skips it. Do not publish without approval.
10. In `auto`, create branch `ai/wiki/<date>`, commit only `/wiki`, push, open GitLab MR.
11. Prepare exact `JIRA TICKET DRAFT` unless mode skips it. Do not create without approval.
12. After `approve confluence`/`approve all`, publish only safe actions listed in the latest approved plan, then update `.doc-map.json`.
13. After `approve jira`/`approve all`, create exactly one Jira review ticket from the latest approved draft.
14. Emit `RELEASE DOCUMENTATION SUMMARY` in French.

## Confluence Safety

Before asking approval, emit exact Confluence target actions per page.
Resolve each page by configured space, configured parent, `.doc-map.json` pageId when available, then exact normalized title.
Classify each page: `UPDATE`, `CREATE`, `SKIP`, `NEEDS MANUAL RESOLUTION`, `REQUIRES DELETION APPROVAL`.
Update only if exactly one safe target exists. Create only if no same-title page exists under target parent. If ambiguous, skip.
Never delete/archive/move unless approval names exact title or page ID.
Do not publish pages not listed in the latest approved `CONFLUENCE PUBLICATION PLAN`.
Never summarize a Confluence page as deleted unless deletion/archive actually completed.

## Jira Safety

Create at most one Jira ticket per run, only after approval.
Before asking approval, emit exact Jira fields. Do not create Jira from an implicit or summarized draft.
Allowed ticket content only: issue type, summary, and description containing MR link, Confluence status/link, and page counts.
Forbidden: labels unless explicitly requested, checklist, MCP diagnostics, internal stages/logs, skipped-stage details, recommendations, failure logs, speculation, sync instructions.

## Output

Emit, when applicable:
1. `WIKI MAP`
2. `EDITORIAL PLAN`
3. `CONFLUENCE PUBLICATION PLAN`
4. `JIRA TICKET DRAFT`
5. `RELEASE DOCUMENTATION SUMMARY`

Final line exactly in French:

`<N> pages, <lien MR ou aucune MR>, terminé`
