# Use AI agents in this repository

Works with any coding assistant. Nothing here depends on a vendor.

## What the agent finds

| File | When it is read |
|---|---|
| `AGENTS.md` (root and per folder) | Always, by tools that support the format. Short context: map, commands, rules, definition of done |
| `.agents/skills/<name>/SKILL.md` | On demand, when the task matches the skill's `description` |
| `docs/` | When a skill or `AGENTS.md` points to a guide |

## Available skills

| Skill | Use for |
|---|---|
| [`feature-builder`](../../.agents/skills/feature-builder/SKILL.md) | Creating a new feature end to end |
| [`data-integrator`](../../.agents/skills/data-integrator/SKILL.md) | TMDB endpoint, DTO, data source, repository, Room |
| [`compose-ui`](../../.agents/skills/compose-ui/SKILL.md) | Screens, components, states, previews, screenshots |
| [`sdui-designer`](../../.agents/skills/sdui-designer/SKILL.md) | New server-driven UI block |
| [`test-engineer`](../../.agents/skills/test-engineer/SKILL.md) | Writing or fixing tests in any layer |
| [`architecture-guardian`](../../.agents/skills/architecture-guardian/SKILL.md) | Reviewing a change against the ADRs and rules |
| [`docs-keeper`](../../.agents/skills/docs-keeper/SKILL.md) | Updating documentation after a change |

## Per tool

| Tool | How to use it |
|---|---|
| Gemini in Android Studio | Reads `AGENTS.md` and `.agents/skills/` automatically (Android Studio Quail or newer). Invoke a skill with `@name` in the chat |
| Codex, Copilot, Cursor, Gemini CLI, Junie and other AGENTS.md-compatible tools | Read `AGENTS.md` automatically. Skills: automatic in tools that support Agent Skills; in others, ask them to "follow `.agents/skills/<name>/SKILL.md`" |
| A tool that reads a different file name | Create an uncommitted local symlink to `AGENTS.md` with the name it expects, or reference `AGENTS.md` in its instructions |
| Chat without repository access | Paste the root `AGENTS.md` and the task's `SKILL.md` at the start of the conversation |

## Asking well

- State the outcome, not the steps: "create the `watchlist` feature with a list and an add button, consuming `account/{id}/watchlist/movies`".
- Point to the skill when the tool doesn't pick it on its own.
- Ask for `./scripts/verify.sh` at the end. An agent that runs the tests fixes its own mistakes; one that doesn't delivers code that doesn't compile.
- Review the diff as you would a person's: the build enforces the rules, not the quality of the business logic.

## Security

- Run `./scripts/install-git-hooks.sh`: the hook blocks `local.properties`, `google-services.json` and tokens even if an agent tries to commit them.
- Don't paste the TMDB token or `google-services.json` into the conversation with the agent.
- Ask for confirmation before the agent runs commands that publish something (push, release).
