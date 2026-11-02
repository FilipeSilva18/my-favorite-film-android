# app/ — composition root

Complementa o [AGENTS.md da raiz](../AGENTS.md).

O `:app` só monta o grafo e a navegação. Não tem regra de negócio, não chama repositório e não tem tela de feature.

## Onde fica cada coisa

| Arquivo | Papel |
|---|---|
| `di/AppComponent.kt` | `@Singleton @Component` com os módulos de core e um `<Feature>DataModule` por feature |
| `di/ViewModelComponent.kt` | `@Subcomponent` por ViewModel (recebe `SavedStateHandle`) com um `<Feature>PresentationModule` por feature |
| `navigation/MoviesNavHost.kt` | Registra as extensões `NavGraphBuilder.<tela>Screen()` das features |
| `navigation/TopLevelDestination.kt` | Abas da bottom bar |
| `navigation/MoviesApp.kt` | `Scaffold` com `NavigationBar` e insets |
| `MoviesApplication.kt` | Cria o `AppComponent` e instala o App Check |
| `src/debug`, `src/release` | Variações por build type (Logcat tracker, `DebugOverlay`), sem `if (BuildConfig.DEBUG)` |

## Ao adicionar uma feature

1. `implementation(projects.feature.<nome>.presentation)` e `implementation(projects.feature.<nome>.data)` em `app/build.gradle.kts`.
2. `<Nome>DataModule::class` em `AppComponent.modules`.
3. `<Nome>PresentationModule::class` em `ViewModelComponent.modules`.
4. `<tela>Screen(...)` no `MoviesNavHost`; se for aba, entrada em `TopLevelDestination`.
5. ViewModel novo no `DependencyGraphTest` (`src/test/.../di/`).

## Não fazer

- Importar classes `internal` de outros módulos ou chamar repositórios daqui.
- Versionar `google-services.json`. Os plugins `google-services` e `crashlytics` só são aplicados quando o arquivo existe.
- Usar Hilt ou anotações de agregação automática (ADR-0009).

## Testes

- `DependencyGraphTest` (Robolectric): todo ViewModel instanciável pelo grafo real.
- `MainActivityViewModelTest`.
- `src/androidTest`: E2E de navegação com Orchestrator (`./gradlew connectedDebugAndroidTest`).
