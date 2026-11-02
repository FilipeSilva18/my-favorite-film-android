# Architecture in diagrams

High-level view. Contracts and code are in [ARCHITECTURE.md](../../ARCHITECTURE.md); decisions are in [decisions/](../decisions/README.md).

## Layers

```mermaid
flowchart LR
    subgraph presentation["presentation (Android + Compose)"]
        Screen["Screen / Content"] --> VM["ViewModel"]
    end
    subgraph domain["domain (pure Kotlin, JVM)"]
        UC["UseCase"] --> RepoI["Repository (interface)"]
    end
    subgraph data["data (Android)"]
        RepoImpl["RepositoryImpl"] --> DS["Local/RemoteDataSource"]
        DS --> Src["Retrofit API / Room DAO / DataStore"]
    end
    VM --> UC
    RepoImpl -. implements .-> RepoI
```

- **Dependency inversion (ADR-0002):** `domain` defines `Repository`; `data` implements it. The dependency arrow points to the domain.
- **ViewModels only use use cases (ADR-0004).** Repositories only use data sources (ADR-0018).
- **UDF:** the ViewModel exposes `StateFlow<UiState>` (or `Flow<PagingData>`); the screen sends events through `onXxx()` functions.

## Module graph

```mermaid
flowchart TD
    app[":app"]

    subgraph movies["feature:movies"]
        mP[presentation] --> mD[domain]
        mDa[data] --> mD
    end
    subgraph detail["feature:moviedetail"]
        dP[presentation] --> dD[domain]
        dDa[data] --> dD
    end
    subgraph favorites["feature:favorites"]
        fApi["api (JVM)"]
        fP[presentation] --> fD[domain]
        fDa[data] --> fD
        fD --> fApi
    end
    subgraph settings["feature:settings"]
        sP[presentation]
    end

    mD & mP --> fApi
    dD & dP --> fApi

    subgraph core["core"]
        common["common (JVM)"]
        cDomain["domain (JVM)"]
        cData[data]
        network[network]
        database[database]
        datastore[datastore]
        ds[designsystem]
        ui[ui]
        di[di]
        analytics["analytics (JVM)"]
        analyticsFb[analytics-firebase]
        ai["ai (JVM)"]
        aiFb[ai-firebase]
    end

    app --> mP & mDa & dP & dDa & fP & fDa & sP
    app --> cData & analyticsFb & aiFb & di

    mP & dP & fP & sP --> ui & ds & di
    sP --> cDomain
    mDa & dDa --> network
    mDa --> ai
    fDa --> database
    cData --> cDomain & datastore
    cDomain --> common
    network --> common
    ui --> ds & common
    analyticsFb --> analytics
    aiFb --> ai
```

Simplified: not every dependency on `core:common`, `core:analytics` and `core:testing` is drawn. The source of truth is the `build.gradle.kts` files and the `moduleGraphAssert` rules in the root build ([reference](../reference/architecture-rules.md)).

**Reading the graph:**
- No arrow between features, except to `favorites:api` (ADR-0005 and ADR-0017).
- No arrow from `core` to `feature`.
- Firebase only in the `-firebase` modules; features see `analytics` and `ai` (JVM contracts).
- `:app` is the only module that knows every feature's `data` and `presentation` (composition root).

## DI graph (pure Dagger)

```mermaid
flowchart TD
    App["MoviesApplication"] --> AC["AppComponent @Singleton"]
    AC --> CoreMods["Core: AppModule, NetworkModule, DatabaseModule, DataStoreModule, DataModule"]
    AC --> Track["AnalyticsModule, FirebaseModule, BuildTypeAnalyticsModule"]
    AC --> AiMod["AiFirebaseModule"]
    AC --> FeatData["MoviesDataModule, MovieDetailDataModule, FavoritesDataModule"]
    AC --> VMB["ViewModelBindingModule"]
    VMB --> VMC["ViewModelComponent (@Subcomponent, one per ViewModel, receives SavedStateHandle)"]
    VMC --> Pres["<Feature>PresentationModule: @Binds @IntoMap @ViewModelKey"]
    Screen["Screen: daggerViewModel()"] --> Factory["DaggerViewModelFactory (core:di)"]
    Factory --> VMC
```

## Flow: paged list

```mermaid
sequenceDiagram
    participant S as MoviesScreen
    participant VM as MoviesViewModel
    participant UC as GetMoviesUseCase
    participant R as MoviesRepositoryImpl
    participant PS as MoviesPagingSource
    participant DS as MoviesRemoteDataSource
    participant API as TMDB

    S->>VM: collectAsLazyPagingItems()
    VM->>UC: invoke(query)
    UC->>R: popularMovies() / searchMovies(query)
    R-->>VM: Flow<PagingData<Movie>> (combined with favorites)
    PS->>DS: popular(page)
    DS->>API: GET movie/popular?page=n
    API-->>PS: PagedResponseDto → LoadResult.Page
```

## Flow: AI search

```mermaid
sequenceDiagram
    participant VM as MoviesViewModel
    participant UC as InterpretMovieQueryUseCase
    participant I as AiMovieQueryInterpreter (data)
    participant G as AiJsonGenerator (core:ai)
    participant F as GeminiJsonGenerator (core:ai-firebase)
    participant D as GetMoviesByFiltersUseCase

    VM->>UC: invoke("90s thriller with great reviews")
    UC->>I: interpret(description, genres)
    I->>G: generate(prompt, AiSchema)
    G->>F: Gemini, structured output, temperature 0
    F-->>I: JSON
    I-->>UC: Understood(MovieFilters) after validation
    UC-->>VM: QueryInterpretation
    VM->>D: discover/movie with the filters
    Note over VM: NotUnderstood or Unavailable → title search
```

## Flow: errors

```mermaid
flowchart LR
    Ex["HttpException / IOException"] --> Safe["safeApiCall (core:network)"]
    Safe --> Res["AppResult.Failure(DomainError)"]
    Safe -. unexpected .-> Rep["ErrorReporter.reportIfUnexpected"]
    Res --> UiErr["UiState.Error"]
    UiErr --> Msg["DomainError.messageRes() → strings.xml"]
```

`CancellationException` is always rethrown. Only `DomainError.Server` and `Unknown` become non-fatal reports in Crashlytics.

## Why this design

| Choice | Gain | Accepted cost |
|---|---|---|
| Module per layer and per feature | The compiler and the graph enforce the boundaries | More modules (convention plugins reduce the boilerplate) |
| Pure Dagger | Explicit graph, compile-time errors | Extra `api(...)` dependencies for `:app`'s KSP |
| JVM contracts for SDKs | Swap vendors without touching features; tests without the SDK | One layer of indirection |
| Rules as tests (Konsist + module-graph-assert) | Executable documentation; agents and people get immediate feedback | Tests to maintain when an ADR changes |
