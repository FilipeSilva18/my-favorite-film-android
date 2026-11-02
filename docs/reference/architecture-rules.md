# Regras de arquitetura executáveis

As regras da arquitetura são verificadas em duas camadas. Quando uma falha, a mensagem aponta o arquivo; a correção é no código, não na regra.

## 1. Grafo de módulos (`./gradlew assertModuleGraph`)

Configurado em `moduleGraphAssert` no `build.gradle.kts` raiz. `-X>` significa "não pode depender de".

| Regra | Decisão |
|---|---|
| `:feature:movies:.* -X> :feature:(moviedetail\|settings):.*` (e equivalentes para cada feature) | ADR-0005 |
| `:feature:(movies\|moviedetail\|settings):.* -X> :feature:favorites:(domain\|data\|presentation)` | ADR-0017 |
| `:feature:favorites:api -X> :feature:favorites:(domain\|data\|presentation)` | ADR-0017 |
| `:feature:.*:api -X> :core:(data\|network\|database\|datastore\|ui\|designsystem\|di\|analytics\|ai)` | ADR-0017 |
| `:core:.* -X> :feature:.*` | ADR-0005 |
| `:feature:.*:presentation -X> .*:data` e `:feature:.*:data -X> .*:presentation` | ADR-0003 |
| `.*:domain -X> :core:(data\|network\|database\|datastore\|ui\|designsystem\|di)` | ADR-0002 |
| `:core:(network\|database\|datastore) -X> :core:(domain\|data)` | ADR-0002 |
| `:feature:.* -X> :core:ai-firebase` | ADR-0016 (mesmo padrão para IA) |
| `:feature:.*:presentation -X> :core:datastore` | ADR-0004 |
| `:core:common -X> :core:.*` | `common` é a base do grafo |
| `maxHeight = 6` | Grafo raso, build paralelo |

## 2. Konsist (`./gradlew :architecture-test:unitTest`)

### `LayerRulesTest`

| Teste | Decisão |
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

| Teste | Decisão |
|---|---|
| `use cases live in domain usecase and expose a single public operator invoke` | Convenções |
| `use case implementations live in domain usecase` | ADR-0017 |
| `repository contracts are interfaces in domain` | ADR-0002 |
| `repository implementations are internal and live in data repository` | ADR-0013 |
| `data sources are internal and live in data datasource` | ADR-0018 |
| `repositories talk to data sources, never to a DAO or a Retrofit API directly` | ADR-0018 |
| `DTOs are internal and live in remote dto` | ADR-0013 |
| `ViewModel subclasses end with ViewModel and live in presentation` | ADR-0001 |
| `ViewModel depends on use cases, never on repositories (ADR-04)` | ADR-0004 |
| `ViewModel never exposes public mutable flows` | Convenções |
| `Compose previews are private and end with Preview` | Convenções |

### `TestCoverageRulesTest`

| Teste | Exigência |
|---|---|
| `every use case has a test` | `<Nome>UseCase` e `<Nome>UseCaseImpl` têm `<Nome>Test` |
| `every ViewModel has a test` | `<Nome>ViewModel` tem `<Nome>ViewModelTest` |

### `UiStringRulesTest`

| Teste | Exigência |
|---|---|
| `composables never show hardcoded text - everything comes from string resources` | Todo texto visível vem de `strings.xml` |

## 3. Outras verificações

| Verificação | Onde |
|---|---|
| Todo ViewModel instanciável pelo grafo Dagger real | `DependencyGraphTest` (`:app`) |
| Screenshots sem regressão | `verifyRoborazziDebug` |
| Links da documentação e frontmatter das skills | `scripts/check-docs.py` |
| Nenhum segredo no commit | `scripts/git-hooks/pre-commit` |

## Mudando uma regra

1. Escreva uma ADR nova em `docs/decisions/` explicando o porquê.
2. Altere o teste ou a regra do grafo e esta tabela no mesmo PR.
3. Atualize o `AGENTS.md` afetado.
