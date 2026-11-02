# architecture-test/ — regras executáveis

Complementa o [AGENTS.md da raiz](../AGENTS.md). Lista completa: [docs/reference/architecture-rules.md](../docs/reference/architecture-rules.md).

Testes [Konsist](https://docs.konsist.lemonappdev.com) que leem o código-fonte de todos os módulos e falham quando uma regra da arquitetura é quebrada. Complementam o `assertModuleGraph` (que vê dependências entre módulos) olhando imports, pacotes, nomes e visibilidade.

| Arquivo | Cobre |
|---|---|
| `LayerRulesTest` | Pureza dos módulos JVM, presentation sem data, feature só via `.api.`, Firebase isolado, IA só pelo contrato |
| `NamingAndVisibilityRulesTest` | Pacotes e nomes de use case, repositório, data source, DTO, ViewModel, previews |
| `TestCoverageRulesTest` | Todo `UseCase`, `UseCaseImpl` e `ViewModel` tem `<Nome>Test` |
| `UiStringRulesTest` | Nenhum texto literal em `@Composable` |
| `KonsistScopes` | Escopos compartilhados e o pacote base |

## Regras

- Quando uma regra falhar, corrija o código. Mudar ou relaxar uma regra exige uma ADR em `docs/decisions/`.
- Regra nova: um teste por regra, nome em inglês entre crases descrevendo o comportamento esperado, e uma linha em `docs/reference/architecture-rules.md`.
- Ao criar uma feature, adicione o nome em `FEATURES` (`KonsistScopes.kt`).
- Rode com `./gradlew :architecture-test:unitTest`.
