---
name: sonar-fix
description: Corrige les problèmes SonarQube, vérifie le build, crée les tickets Jira. Sélectionner et dire lance.
---

# Sonar-Fix

Fix SonarQube issues for this project. Act immediately.

## Absolute rules
1. Issues come ONLY from the sonarqube MCP. Never find issues yourself. No Sonar key = not real.
2. Severity comes from Sonar. Never assign or change it.

## Defaults
- Language: use `language` from AGENTS.md (default French). This applies to EVERYTHING —
  Jira tickets, MR descriptions, AND your chat/terminal messages.
  Override with "in English" in the prompt.
- Fix everything by default. Only security and concurrency issues escalate (Jira only, no fix).
- `cautious` in prompt = only fix MAJOR/MINOR mechanical stuff (unused imports, null checks, etc).

## Do
- FIRST: warm up MCPs — call the sonarqube MCP (e.g. list projects) and the jira MCP
  (e.g. list projects) before doing anything else. If either fails, wait a few seconds
  and retry once. This ensures they're connected before the real work starts.
- Read AGENTS.md for project keys. If missing, generate it and continue.
- Fetch issues via sonarqube MCP.
- Check `max_issues_per_run` in AGENTS.md. If set, take ONLY that many issues from the
  fetch — drop the rest. This is a HARD CAP, not a suggestion. Stop after that number.
- For EACH issue, do all three steps before moving to the next:
    1. Fix: read the file, smallest edit, then verify (mvn compile + test).
       PASS = keep. FAIL = revert completely.
    2. Jira: create a ticket for this issue immediately. Fixed = move to review/test status.
       Escalated/Failed = To Do.
       Include the Sonar issue URL (construct from project key + issue key if needed).
       Use the sonar-fix skill for ticket templates.
    3. Next issue.
- After all issues: if gitlab MCP works, commit to ai/sonar-fix/<date>, push, open MR
  (never merge). MR description = table of what was fixed (rule, file:line, change, Sonar link).
- Print one line: fixed N, failed N, escalated N, MR link, done.

## Do NOT
- Ask questions or wait for approval (unless user said `review`).
- Find issues yourself or rate severity.
- List what you "would" do. Do it.
- Skip an MCP silently. If it errors, say why.
- Merge MRs or push to protected branches.
