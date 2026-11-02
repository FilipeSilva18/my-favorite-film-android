# Executable architecture rules

Architecture rules are checked at two levels. When one fails, the message points to the file; the fix goes in the code, not in the rule.

## 1. Module graph (`./gradlew assertModuleGraph`)

Configured in `moduleGraphAssert` in the root `build.gradle.kts`. `-X>` means "must not depend on".

| Rule | Decision |
|---|---|
| `:feature:movies:.* -X> :feature:(moviedetail\|settings):.*` (and equivalents for each feature) | ADR-0005 |
| `:feature:(movies\|moviedetail\|settings):.* -X> :feature:favorites:(domain\|data\|presentation)` | ADR-0017 |
| `:feature:favorites:api -X> :feature:favorites:(domain\|data\|presentation)` | ADR-0017 |
| `:feature:.*:api -X> :core:(data\|network\|database\|datastore\|ui\|designsystem\|di\|analytics\|ai)` | ADR-0017 |
| `:core:.* -X> :feature:.*` | ADR-0005 |
| `:feature:.*:presentation -X> .*:data` and `:feature:.*:data -X> .*:presentation` | ADR-0003 |
| `.*:domain -X> :core:(data\|network\|database\|datastore\|ui\|designsystem\|di)` | ADR-0002 |
| `:core:(network\|database\|datastore) -X> :core:(domain\|data)` | ADR-0002 |
| `:feature:.* -X> :core:ai-firebase` | ADR-0016 (same pattern for AI) |
| `:feature:.*:presentation -X> :core:datastore` | ADR-0004 |
| `:core:common -X> :core:.*` | `common` is the base of the graph |
| `maxHeight = 6` | Shallow graph, parallel build |

## 2. Konsist (`./gradlew :architecture-test:unitTest`)

### `LayerRulesTest`

| Test | Decision |
|---|---|
| `domain, common and the AI contract are pure Kotlin - no Android, Compose, Room, Retrofit, OkHttp or Dagger` | ADR-0002 |
| `presentation never imports the data layer` | ADR-0003 |
| `a feature only reaches another feature through its public api module (ADR-05)` | ADR-0005, ADR-0017 |
| `public api modules only hold contracts - no Android, Compose, Room, Retrofit or Dagger` | ADR-0017 |
| `core never imports features` | ADR-0005 |
| `android util Log only in the debug source set` | ADR-0016 |
| `only analytics-firebase and ai-firebase know the Firebase SDK` | ADR-0016 |
| `features talk to AI only through the core ai contract` | ARCHITECTURE.md 7.13 |

### `NamingAndVisibilityRulesTest`

| Test | Decision |
|---|---|
| `use cases live in domain usecase and expose a single public operator invoke` | Conventions |
| `use case implementations live in domain usecase` | ADR-0017 |
| `repository contracts are interfaces in domain` | ADR-0002 |
| `repository implementations are internal and live in data repository` | ADR-0013 |
| `data sources are internal and live in data datasource` | ADR-0018 |
| `repositories talk to data sources, never to a DAO or a Retrofit API directly` | ADR-0018 |
| `DTOs are internal and live in remote dto` | ADR-0013 |
| `ViewModel subclasses end with ViewModel and live in presentation` | ADR-0001 |
| `ViewModel depends on use cases, never on repositories (ADR-04)` | ADR-0004 |
| `ViewModel never exposes public mutable flows` | Conventions |
| `Compose previews are private and end with Preview` | Conventions |

### `TestCoverageRulesTest`

| Test | Requirement |
|---|---|
| `every use case has a test` | `<Name>UseCase` and `<Name>UseCaseImpl` have a `<Name>Test` |
| `every ViewModel has a test` | `<Name>ViewModel` has a `<Name>ViewModelTest` |

### `UiStringRulesTest`

| Test | Requirement |
|---|---|
| `composables never show hardcoded text - everything comes from string resources` | Every visible text comes from `strings.xml` |

## 3. Other checks

| Check | Where |
|---|---|
| Every ViewModel can be created by the real Dagger graph | `DependencyGraphTest` (`:app`) |
| No screenshot regressions | `verifyRoborazziDebug` |
| Documentation links and skills' frontmatter | `scripts/check-docs.py` |
| No secrets in commits | `scripts/git-hooks/pre-commit` |

## Changing a rule

1. Write a new ADR in `docs/decisions/` explaining why.
2. Change the test or the graph rule and this table in the same PR.
3. Update the affected `AGENTS.md`.
