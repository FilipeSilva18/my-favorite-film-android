# Convenções de código

Resumo para consulta. A versão completa, com exemplos, está na seção 14 do [ARCHITECTURE.md](../../ARCHITECTURE.md#14-convenções-de-código).

## Nomes

| Elemento | Padrão | Exemplo |
|---|---|---|
| Rota | `<Tela>Route` (`@Serializable`) | `MovieDetailRoute(movieId)` |
| Composable com ViewModel | `<Tela>Screen` | `MovieDetailScreen` |
| Composable sem estado | `<Tela>Content` | `MovieDetailContent` |
| Estado de tela | `<Tela>UiState` (sealed interface) | `MovieDetailUiState` |
| Modelo de UI | `<Nome>Ui` | `MovieCardUi` |
| Use case | Verbo + substantivo + `UseCase` | `GetMovieDetailUseCase` |
| Implementação de use case do `api` | `<Nome>UseCaseImpl` | `ToggleFavoriteUseCaseImpl` |
| Repositório | `<Nome>Repository` / `<Nome>RepositoryImpl` (`internal`) | `MoviesRepositoryImpl` |
| Data source | `<Tipo>LocalDataSource` / `<Tipo>RemoteDataSource` (`internal`) | `MoviesRemoteDataSource` |
| DTO | `<Nome>Dto` (`internal`) | `MovieDto` |
| Entity | `<Nome>Entity` | `FavoriteMovieEntity` |
| Módulo Dagger | `<Feature><Camada>Module` público + `<Feature><Camada>BindsModule` interno | `MoviesDataModule` |
| Navegação | `NavGraphBuilder.<tela>Screen()` / `NavController.navigateTo<Tela>()` | `movieDetailScreen()` |
| Preview | `private fun <Nome>Preview()` com `@Preview` | `<Tela>Content<Estado>Preview` |
| Teste | `<Classe>Test`, mesmo pacote | `MoviesViewModelTest` |
| Fake | `Fake<Contrato>` / `Test<Contrato>` | `FakeFavorites`, `TestAnalytics` |

## Regras

- **Visibilidade:** `internal` por padrão. Público só o contrato do módulo.
- **Imutabilidade:** `UiState` e modelos são `data class` com `val` e `List`.
- **Use case:** uma responsabilidade, `operator fun invoke`, sem estado.
- **ViewModel:** expõe `StateFlow` ou `Flow<PagingData>`, recebe eventos por `onXxx()`, nunca expõe `MutableStateFlow`.
- **Compose:** `*Content` recebe estado e lambdas, sem ViewModel; previews de cada estado.
- **Strings:** `values/strings.xml` (pt-BR) e `values-en/strings.xml`, inclusive `contentDescription` e unidades.
- **Coroutines:** nunca capturar `CancellationException` sem relançar.
- **Erros:** dados retornam `AppResult`; a UI mostra `DomainError.messageRes()`, nunca `e.message`.
- **Comentários:** nenhum comentário explicativo no código; o porquê vai para ADRs.
- **Idioma:** código, testes, mensagens de build e CI em inglês; documentação em português.
- **Testes:** JVM com nomes entre crases (`` `blank query returns popular movies` ``); instrumentados em camelCase. Fakes, nunca mocks.
- **Dependências:** versões só no `gradle/libs.versions.toml`; `api(...)` só quando o tipo aparece na API pública do módulo (ou para o KSP do `:app`).
