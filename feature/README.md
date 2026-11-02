# feature/

Passo a passo completo: [docs/how-to/add-feature.md](../docs/how-to/add-feature.md). Agentes: skill [`feature-builder`](../.agents/skills/feature-builder/SKILL.md).

Features atuais: `movies` (lista + busca com IA), `moviedetail` (detalhe + SDUI), `favorites` (`api` + `domain`/`data`/`presentation`, ADR-17) e `settings` (só presentation).

**Antes de criar UI, reaproveite o core:** `MovieCard`/`MovieGrid`/skeletons (`core:ui/movie`),
`LoadingContent`/`ErrorContent`/`EmptyContent` (`core:ui`), `MoviePoster`/`SearchField`/`LabelChips`
(`core:designsystem`) e `sduiItems(...)` para blocos server-driven (`core:ui/sdui`).

Cada feature vive em até três módulos Gradle (ADR-03 do ARCHITECTURE.md):

```
feature/<nome>/
├── domain/        plugin movies.jvm.library (+ movies.dagger se tiver @Inject)
├── data/          plugin movies.android.feature.data
└── presentation/  plugin movies.android.feature.presentation
```

## Checklist para adicionar uma feature

1. Criar os três módulos e fazer os `include` em `settings.gradle.kts`.
   Acrescentar a feature às regras `-X>` do `moduleGraphAssert` (build raiz) e a `FEATURES` em `architecture-test/.../KonsistScopes.kt`.
2. **domain**: modelos, interface de repositório, use cases com `@Inject constructor`.
3. **data**: DTOs `internal`, interface Retrofit `internal`, `RepositoryImpl` `internal`.
   O repositório não recebe a API Retrofit nem o DAO direto: crie um `<Tipo>RemoteDataSource`
   (ou `<Tipo>LocalDataSource` para Room) `internal` em `data/datasource`, que fala só com aquela
   fonte e devolve DTO/entity. O repositório depende do data source e mapeia para o domínio (ADR-18).
   Módulo Dagger no padrão público + interno:
   ```kotlin
   @Module(includes = [MoviesDataBindsModule::class])
   object MoviesDataModule

   @Module
   internal interface MoviesDataBindsModule {
       @Binds fun bindRepository(impl: MoviesRepositoryImpl): MoviesRepository
       companion object {
           @Provides fun provideApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)
       }
   }
   ```
   e adicionar `MoviesDataModule::class` na lista de `modules` do `AppComponent`.
4. **presentation**: ViewModel com `@Inject constructor` (pode pedir `SavedStateHandle`),
   módulo de ViewModel:
   ```kotlin
   @Module
   interface MoviesPresentationModule {
       @Binds @IntoMap @ViewModelKey(MoviesViewModel::class)
       fun bindMoviesViewModel(vm: MoviesViewModel): ViewModel
   }
   ```
   e adicionar `MoviesPresentationModule::class` no `ViewModelComponent` do `:app`.
   Na tela: `viewModel: MoviesViewModel = daggerViewModel()`.
5. **navegação**: rota `@Serializable`, `NavGraphBuilder.<tela>Screen()` e
   `NavController.navigateTo<Tela>()` em `presentation/navigation/`; registrar no `MoviesNavHost`.
6. `:app` passa a depender de `feature:<nome>:presentation` e `feature:<nome>:data`.
7. Se for uma aba nova: entrada no enum `TopLevelDestination` (`app/navigation`).

Feature que só usa regras do core (como `settings`) pode ter **só o módulo presentation**,
com `api(projects.core.domain)`.

**Feature consumida por outras features** (como `favorites`) ganha um módulo `api` além dos três de camada (ADR-17):

```
feature/<nome>/
├── api/           plugin movies.jvm.library + `java-test-fixtures`: modelos e interfaces finas de use case; fakes em src/testFixtures
├── domain/        implementa as interfaces do api (`<Nome>UseCaseImpl`) + use cases internos da feature
├── data/          repositório + `<Nome>DataModule`, que liga as interfaces do api às implementações
└── presentation/  tela, ViewModel e navegação
```

- Consumidores declaram `api(projects.feature.<nome>.api)` no próprio `domain` e, nos testes, `testImplementation(testFixtures(projects.feature.<nome>.api))`.
- `<Nome>UseCaseImpl` fica em `domain/usecase` e precisa de `<Nome>UseCaseImplTest` (o Konsist cobra). O binding fica no `data`, porque o `domain` não pode ter Dagger.
- Outra feature nunca depende do `domain`, `data` ou `presentation` desta: o module-graph-assert falha.

> **Atenção (Dagger puro):** `data` e `presentation` declaram o `domain` da feature como **`api`**,
> não `implementation`. Os componentes Dagger são gerados no `:app` e precisam enxergar os tipos
> do domain (repositório, use cases). Sem isso: `ComponentProcessingStep was unable to process
> AppComponent because '...Repository' could not be resolved`.

## Testes de uma feature nova (o Konsist cobra os dois primeiros)

1. `<Nome>UseCaseTest` para cada use case (domain, fakes).
2. `<Nome>ViewModelTest` (Turbine / `asSnapshot`, `MainDispatcherRule`).
3. Data: teste de contrato com MockWebServer + JSON real em `src/test/resources/tmdb/`.
4. `<Tela>ContentTest` (Compose + Robolectric) cobrindo dados, loading, erro e vazio.
5. `<Tela>ScreenshotTest` (Roborazzi, claro/escuro) e `./gradlew recordRoborazziDebug`.
6. Registrar o ViewModel novo no `DependencyGraphTest` do `:app`.

## Tracking numa feature nova

- Tela: `TrackScreenView(ScreenNames.X)` no topo do `*Screen` (adicione o nome em `ScreenNames`).
- Ações: injete `Analytics` no ViewModel e chame `analytics.track(...)` com uma fábrica de `core:analytics`.
- Erros inesperados no data: `safeApiCall(onFailure = { c, e -> errorReporter.reportIfUnexpected(c, e, "origem") })`.
- Nunca importe `com.google.firebase.*` fora de `core:analytics-firebase` e `core:ai-firebase` (o Konsist falha). Para usar IA numa feature, dependa só de `core:ai` (`AiJsonGenerator`) e faça a implementação do contrato do domain no `data`, como o `AiMovieQueryInterpreter`.
