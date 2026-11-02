# Criar uma feature

Exemplo: feature `watchlist` com lista e ação de adicionar. Checklist compacto: [feature/README.md](../../feature/README.md). Skill: [`feature-builder`](../../.agents/skills/feature-builder/SKILL.md).

## 0. Decidir o formato

| Pergunta | Resposta → formato |
|---|---|
| Tem regra de negócio própria ou fonte de dados? | Sim → `domain` + `data` + `presentation` |
| Só exibe e altera regras que já estão no `core:domain`? | Só `presentation` (como `settings`) |
| Outra feature precisa usar algo dela? | Acrescente `api` (como `favorites`, ADR-0017) |
| Duas features vão precisar da mesma regra transversal (não de uma feature)? | Regra no `core` |

## 1. Módulos

```
feature/watchlist/
├── domain/build.gradle.kts         plugins: movies.jvm.library (+ movies.dagger)
├── data/build.gradle.kts           plugins: movies.android.feature.data       → api(projects.feature.watchlist.domain)
└── presentation/build.gradle.kts   plugins: movies.android.feature.presentation → api(projects.feature.watchlist.domain)
```

- `include(":feature:watchlist:domain")` etc. em `settings.gradle.kts`.
- Namespace Android: `com.faas.myfavoritefilms.feature.watchlist.data` / `.presentation`.
- Regras no `moduleGraphAssert`: a nova feature não depende das outras, e as outras não dependem dela.
- `"watchlist"` em `FEATURES` (`architecture-test/.../KonsistScopes.kt`).

## 2. domain

`model/`, `repository/WatchlistRepository` (interface), `usecase/` com `@Inject constructor` e um `operator fun invoke`. Sem Android.

## 3. data

Siga [add-endpoint.md](add-endpoint.md) para cada fonte remota. Fonte local: entity e DAO em `core:database`, `WatchlistLocalDataSource` em `datasource/`. Módulo Dagger:

```kotlin
@Module(includes = [WatchlistDataBindsModule::class])
object WatchlistDataModule

@Module
internal interface WatchlistDataBindsModule {
    @Binds fun bindRepository(impl: WatchlistRepositoryImpl): WatchlistRepository
}
```

## 4. presentation

- `WatchlistUiState` (sealed interface), `WatchlistViewModel` com `@Inject constructor` só com use cases (e `Analytics`, `SavedStateHandle` se precisar).
- `WatchlistScreen` (pega o ViewModel com `daggerViewModel()`, coleta com `collectAsStateWithLifecycle`) e `WatchlistContent` (sem estado, previews).
- Reaproveite `core:ui` e `core:designsystem` antes de criar componentes.
- `TrackScreenView(ScreenNames.WATCHLIST)`.
- `di/WatchlistPresentationModule`: `@Binds @IntoMap @ViewModelKey(WatchlistViewModel::class)`.
- `navigation/`: `@Serializable data object WatchlistRoute`, `NavGraphBuilder.watchlistScreen(...)`, `NavController.navigateToWatchlist()`.
- Strings em `values/` e `values-en/`.

## 5. app

- `implementation` de `presentation` e `data` em `app/build.gradle.kts`.
- `WatchlistDataModule::class` no `AppComponent`; `WatchlistPresentationModule::class` no `ViewModelComponent`.
- `watchlistScreen(...)` no `MoviesNavHost`; aba nova → `TopLevelDestination`.
- ViewModel no `DependencyGraphTest`.

## 6. Testes

Use case, ViewModel, repositório (MockWebServer ou Room), `WatchlistContentTest`, `WatchlistScreenshotTest`. Detalhes em [write-tests.md](write-tests.md).

## 7. Documentação

- `ARCHITECTURE.md`: escopo (seção 1), estrutura (seção 4), seção da feature.
- `feature/AGENTS.md`: linha na tabela de features.
- ADR se a feature introduz um padrão novo.

## 8. Verificar

```bash
./scripts/verify.sh
```
