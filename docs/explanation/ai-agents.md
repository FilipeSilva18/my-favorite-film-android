# AI agents in this repository

How the project is prepared to be developed with AI agents from any vendor, and where it can evolve.

## Principle

An agent makes fewer mistakes when three things exist in the repository:

1. **Short, always-loaded context:** what the project is, how to run it, what never to do. → `AGENTS.md`.
2. **On-demand procedures:** how to do each recurring task the project's way. → Skills in `.agents/skills/`.
3. **Automatic verification:** the agent finds out on its own when it breaks a rule, without relying on human review. → `assertModuleGraph`, Konsist, tests, `scripts/verify.sh`.

The third item matters most. Unverified documentation goes stale; a rule that becomes a test guides the agent through a compilation error or a red test.

## What already exists

| Layer | Artifact | Open standard |
|---|---|---|
| Context | `AGENTS.md` at the root and in `app/`, `core/`, `feature/`, `build-logic/`, `architecture-test/` | [AGENTS.md](https://agents.md) (Agentic AI Foundation / Linux Foundation) |
| Procedures | 7 skills: `feature-builder`, `data-integrator`, `compose-ui`, `sdui-designer`, `test-engineer`, `architecture-guardian`, `docs-keeper` | [Agent Skills](https://agentskills.io) |
| Knowledge | `README.md`, `docs/` (Diátaxis), MADR ADRs, `ARCHITECTURE.md` | [MADR](https://adr.github.io/madr/), [Diátaxis](https://diataxis.fr) |
| Guardrails | module-graph-assert, 21 Konsist rules, mandatory use case and ViewModel tests, string rule | — |
| Security | `scripts/git-hooks/pre-commit` blocks `local.properties`, `google-services.json` and tokens in the diff | — |
| Feedback | `scripts/verify.sh`, `scripts/verify-module.sh`, `scripts/check-docs.py` | — |
| PR flow | PR template with the definition of done; issue template with a feature specification | — |

**Why these standards:** `AGENTS.md` is read by Gemini in Android Studio, Codex, Copilot, Cursor, Gemini CLI, Junie and dozens of other tools. `.agents/skills/` is the directory Android Studio (from Quail on) and Codex look in, and Google publishes official Android skills in this format. No file is vendor-specific.

## Agent roles as skills

There is no open standard for "subagents": each tool has its own format. That is why the roles are skills, which any compatible tool loads when the description matches the task, and which a person can paste as a prompt into a tool without support.

| Skill | Responsibility | Boundary |
|---|---|---|
| `feature-builder` | New feature end to end (modules, DI, navigation, tests) | Orchestrates; delegates details to the other skills |
| `data-integrator` | TMDB endpoint, DTO, data source, repository, contract test | Only the `data` layer and the contract in `domain` |
| `compose-ui` | Compose screens and components, states, strings, previews, screenshots | Only `presentation`, `core:ui`, `core:designsystem` |
| `sdui-designer` | New block in the server-driven UI vocabulary | `core:common/sdui`, `core:network/sdui`, `core:ui/sdui` |
| `test-engineer` | Tests in every layer, fakes, required coverage | Doesn't change production code without asking |
| `architecture-guardian` | Reviews changes against ADRs and rules; evolves the rules | Reads and points out; changes a rule only with an ADR |
| `docs-keeper` | Keeps `README.md`, `AGENTS.md`, `docs/`, ADRs and `ARCHITECTURE.md` consistent with the code | Documentation only |

## Roadmap: where to invest

Ideas ordered by value versus effort.

### 1. Deterministic generators (scaffolding)

A `scripts/new-feature.sh <name> [--with-api]` script that creates the modules, `build.gradle.kts` files, packages, Dagger modules, route, `include` and empty tests from templates. The `feature-builder` skill calls the script instead of writing the boilerplate.
**Why:** agents spend context and make mistakes on repetitive code; a generator is deterministic and testable. The agent keeps the part that needs judgment (business rules, UI).

### 2. Agent PR review in CI

A job that runs on every PR, sends the diff and the `architecture-guardian` skill to a model (the vendor is an environment variable) and comments on ADR violations the tests don't catch: poor names, a use case with two responsibilities, strings off the tone guide.
**Requirements:** a secret with the API key, a cost limit per PR, comments as suggestions only (never blocking the merge on its own).

### 3. Skill evaluation (evals)

A set of reference tasks ("add the `movie/{id}/reviews` endpoint", "create the `watchlist` feature with an `api` module") with an objective success criterion: `./scripts/verify.sh` passes and the diff respects the rules. Running them periodically with more than one model shows when a skill or an `AGENTS.md` has become ambiguous.

### 4. Project MCP server

An [MCP](https://modelcontextprotocol.io) server (another Agentic AI Foundation standard) with repository-specific tools: `module_graph` (current graph as JSON), `explain_rule` (from a Konsist rule to its ADR), `run_verify` (structured result instead of a log). Any MCP client can then query the project without reading whole files.

### 5. Specification before code

An issue template with a structured specification: goal, screens, states, endpoints, analytics events, acceptance criteria. An agent turns the issue into a plan and a PR; the specification becomes the review contract.

### 6. Verified documentation

Extend `scripts/check-docs.py` to compare documentation facts with the code: the module list in `settings.gradle.kts`, the Konsist feature list, the plugins registered in `build-logic`. Diverging documentation breaks CI.

### 7. Agent quality telemetry

Mark PRs made with an agent (label or commit trailer) and measure the first-try green CI rate, the number of review rounds and the most violated rules. The data shows which skill or rule needs adjusting.
