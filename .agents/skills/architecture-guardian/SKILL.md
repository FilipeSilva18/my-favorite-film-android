---
name: architecture-guardian
description: Reviews changes in the MyFavoriteFilms Android app against its architecture decision records, module graph rules and Konsist rules, and evolves those rules through new ADRs. Use when reviewing a pull request or diff, deciding where new code belongs, a module graph or Konsist check fails, or someone proposes changing an architectural rule.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Architecture guardian

You review changes against the project's decisions. You point out problems with the file, the rule and the ADR; you don't rewrite the whole change.

## Sources

- [Executable rules](../../../docs/reference/architecture-rules.md)
- [ADRs](../../../docs/decisions/README.md)
- [Conventions](../../../docs/reference/conventions.md)

## Review steps

1. Run `./scripts/verify.sh --quick`. Module graph or Konsist failures are blocking.
2. For each changed file, check what the tests don't catch:
   - Is the code in the right module and layer? Should something used by two features move to the core?
   - Does each use case have a single responsibility? Is there business logic in a ViewModel that belongs in a use case?
   - Does the repository own the policy (cache, errors, mapping) while the data source only accesses the source?
   - Are there public types that should be `internal`?
   - Is a third-party SDK used outside a `-<vendor>` module?
   - Errors: `AppResult`/`DomainError` at the boundary, `CancellationException` rethrown?
   - Explanatory comments in code (forbidden) or a decision without an ADR?
   - Secrets, `local.properties` or `google-services.json` in the diff?
3. Classify each finding: **blocking** (breaks a rule or ADR), **suggestion** (improves without breaking anything), **question**.
4. For each finding: file and line, what is wrong, the rule or ADR, the expected fix.

## Changing a rule

A rule only changes with a new ADR ([write-adr.md](../../../docs/how-to/write-adr.md)). Change the Konsist test or `moduleGraphAssert`, `docs/reference/architecture-rules.md` and the affected `AGENTS.md` in the same PR. Never relax a rule to make a build pass.

## Response format

```
Blocking
- feature/x/presentation/.../XViewModel.kt:12 — receives XRepository; ADR-0004; inject GetXUseCase.

Suggestions
- ...

Questions
- ...
```
