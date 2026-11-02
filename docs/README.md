# Documentation

Organized with the [Diátaxis](https://diataxis.fr) framework: each document has a single purpose. Written for people and AI agents alike; no document depends on a specific tool.

| Type | Purpose | Documents |
|---|---|---|
| **Tutorial** | Learn by doing, from scratch | [Onboarding](tutorials/onboarding.md) |
| **How-to** | Solve a concrete task | [New feature](how-to/add-feature.md) · [New TMDB endpoint](how-to/add-endpoint.md) · [New SDUI block](how-to/add-sdui-block.md) · [New analytics event](how-to/add-analytics-event.md) · [Write tests](how-to/write-tests.md) · [Record an ADR](how-to/write-adr.md) · [Use AI agents](how-to/use-ai-agents.md) |
| **Reference** | Look up facts | [Modules](reference/modules.md) · [Commands](reference/commands.md) · [Architecture rules](reference/architecture-rules.md) · [Code conventions](reference/conventions.md) |
| **Explanation** | Understand the why | [Architecture in diagrams](explanation/architecture.md) · [AI agents in this repository](explanation/ai-agents.md) · [ADRs](decisions/README.md) |

Documents outside this folder:

- [`README.md`](../README.md): project overview.
- [`AGENTS.md`](../AGENTS.md) at the root and in `app/`, `core/`, `feature/`, `build-logic/`, `architecture-test/`: short instructions that agents read automatically.
- [`ARCHITECTURE.md`](../ARCHITECTURE.md): complete architecture document, with contracts and code excerpts.
- [`feature/README.md`](../feature/README.md): new feature checklist.
- [`.agents/skills/`](../.agents/skills): agent skills in the open Agent Skills format.

## Standards in use

| Standard | Where | Why |
|---|---|---|
| [AGENTS.md](https://agents.md) | Nested `AGENTS.md` files | Open format maintained by the Agentic AI Foundation (Linux Foundation), read by Android Studio (Gemini), Codex, Copilot, Cursor, Gemini CLI and others |
| [Agent Skills](https://agentskills.io) | `.agents/skills/<name>/SKILL.md` | Open standard for on-demand instructions; `.agents/skills` is the directory read by Android Studio and Codex |
| [MADR 4](https://adr.github.io/madr/) | `docs/decisions/` | Lean, widely used ADR format with a confirmation section |
| [Diátaxis](https://diataxis.fr) | `docs/` | Separates tutorial, how-to, reference and explanation |
| [Mermaid](https://mermaid.js.org) | Diagrams | Renders on GitHub and in Android Studio, versioned as text |

## Maintenance

- When code and documentation disagree, the code is the source of truth; fix the documentation in the same PR.
- `./scripts/check-docs.py` validates relative links and the skills' frontmatter; it runs in CI.
- Responsible agent: the [`docs-keeper`](../.agents/skills/docs-keeper/SKILL.md) skill.
