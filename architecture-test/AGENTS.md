# architecture-test/ — executable rules

Complements the [root AGENTS.md](../AGENTS.md). Full list: [docs/reference/architecture-rules.md](../docs/reference/architecture-rules.md).

[Konsist](https://docs.konsist.lemonappdev.com) tests that read the source code of every module and fail when an architecture rule is broken. They complement `assertModuleGraph` (which sees dependencies between modules) by looking at imports, packages, names and visibility.

| File | Covers |
|---|---|
| `LayerRulesTest` | Purity of JVM modules, presentation without data, features only through `.api.`, isolated Firebase, AI only through the contract |
| `NamingAndVisibilityRulesTest` | Packages and names of use cases, repositories, data sources, DTOs, ViewModels, previews |
| `TestCoverageRulesTest` | Every `UseCase`, `UseCaseImpl` and `ViewModel` has a `<Name>Test` |
| `UiStringRulesTest` | No literal text in a `@Composable` |
| `KonsistScopes` | Shared scopes and the base package |

## Rules

- When a rule fails, fix the code. Changing or relaxing a rule requires an ADR in `docs/decisions/`.
- New rule: one test per rule, named in English in backticks describing the expected behavior, and a row in `docs/reference/architecture-rules.md`.
- When creating a feature, add its name to `FEATURES` (`KonsistScopes.kt`).
- Run with `./gradlew :architecture-test:unitTest`.
