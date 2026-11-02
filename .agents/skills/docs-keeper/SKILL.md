---
name: docs-keeper
description: Keeps the MyFavoriteFilms documentation in sync with the code - README, AGENTS.md files, ARCHITECTURE.md, docs folder organized by Diataxis, MADR architecture decision records, module reference, commands and agent skills. Use after any change to modules, rules, conventions, commands or features, when asked to document something, or when documentation and code disagree.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Docs keeper

You keep the documentation correct. The code is the source of truth.

## Where each piece of information lives

| Changed | Update |
|---|---|
| New or removed module | `docs/reference/modules.md`, section 4 of `ARCHITECTURE.md`, the folder's `AGENTS.md`, diagrams in `docs/explanation/architecture.md` and `README.md` |
| New feature | Section 1 and the feature's own section in `ARCHITECTURE.md`, `feature/AGENTS.md`, features table in `README.md` |
| New decision | `docs/decisions/NNNN-*.md`, index in `docs/decisions/README.md`, table in section 2 of `ARCHITECTURE.md` |
| Konsist or graph rule | `docs/reference/architecture-rules.md`, affected `AGENTS.md` |
| Command or script | `docs/reference/commands.md`, root `AGENTS.md` |
| Convention | `docs/reference/conventions.md`, section 14 of `ARCHITECTURE.md` |
| Recurring procedure | Guide in `docs/how-to/` and, if it is an agent task, a skill in `.agents/skills/` |

## Writing rules

- English, short sentences, precise technical terms (no analogies).
- Each document has one Diátaxis type: tutorial, how-to, reference or explanation. Don't mix them.
- Keep `AGENTS.md` short (under 150 lines): link to `docs/` instead of repeating.
- `SKILL.md`: `name` in the frontmatter equal to the folder name, `description` saying what it does and when to use it (up to 1024 characters), body under 500 lines.
- An accepted ADR is never rewritten; create a new one and mark the old one `superseded by`.
- No reference to a specific AI tool as a requirement.
- Diagrams in Mermaid.
- No secret, token or `google-services.json` content in examples.

## Verify

```bash
./scripts/check-docs.py
```

Also spot-check that the classes, files and commands mentioned exist in the code.
