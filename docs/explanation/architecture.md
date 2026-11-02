# Arquitetura em diagramas

Visão de alto nível. Os contratos e o código estão no [ARCHITECTURE.md](../../ARCHITECTURE.md); as decisões, em [decisions/](../decisions/README.md).

## Camadas

```mermaid
flowchart LR
    subgraph presentation["presentation (Android + Compose)"]
        Screen["Screen / Content"] --> VM["ViewModel"]
    end
    subgraph domain["domain (Kotlin puro, JVM)"]
        UC["UseCase"] --> RepoI["Repository (interface)"]
    end
    subgraph data["data (Android)"]
        RepoImpl["RepositoryImpl"] --> DS["Local/RemoteDataSource"]
        DS --> Src["Retrofit API / Room DAO / DataStore"]
    end
    VM --> UC
    RepoImpl -. implementa .-> RepoI
```

- **Dependency Inversion (ADR-0002):** o `domain` define `Repository`; o `data` implementa. A seta de dependência aponta para o domínio.
- **ViewModel só com use cases (ADR-0004).** Repositório só com data sources (ADR-0018).
- **UDF:** o ViewModel expõe `StateFlow<UiState>` (ou `Flow<PagingData>`); a tela envia eventos por funções `onXxx()`.

## Grafo de módulos

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

Simplificado: as dependências de `core:common`, `core:analytics` e `core:testing` não estão todas desenhadas. A fonte da verdade são os `build.gradle.kts` e as regras do `moduleGraphAssert` no build raiz ([referência](../reference/architecture-rules.md)).

**Leitura do grafo:**
- Nenhuma seta entre features, exceto para `favorites:api` (ADR-0005 e ADR-0017).
- Nenhuma seta de `core` para `feature`.
- Firebase só nos módulos `-firebase`; as features veem `analytics` e `ai` (contratos JVM).
- O `:app` é o único que conhece `data` e `presentation` de todas as features (composition root).

## Grafo de DI (Dagger puro)

```mermaid
flowchart TD
    App["MoviesApplication"] --> AC["AppComponent @Singleton"]
    AC --> CoreMods["Core: AppModule, NetworkModule, DatabaseModule, DataStoreModule, DataModule"]
    AC --> Track["AnalyticsModule, FirebaseModule, BuildTypeAnalyticsModule"]
    AC --> AiMod["AiFirebaseModule"]
    AC --> FeatData["MoviesDataModule, MovieDetailDataModule, FavoritesDataModule"]
    AC --> VMB["ViewModelBindingModule"]
    VMB --> VMC["ViewModelComponent (@Subcomponent, um por ViewModel, recebe SavedStateHandle)"]
    VMC --> Pres["<Feature>PresentationModule: @Binds @IntoMap @ViewModelKey"]
    Screen["Screen: daggerViewModel()"] --> Factory["DaggerViewModelFactory (core:di)"]
    Factory --> VMC
```

## Fluxo: lista paginada

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
    R-->>VM: Flow<PagingData<Movie>> (combinado com favoritos)
    PS->>DS: popular(page)
    DS->>API: GET movie/popular?page=n
    API-->>PS: PagedResponseDto → LoadResult.Page
```

## Fluxo: busca com IA

```mermaid
sequenceDiagram
    participant VM as MoviesViewModel
    participant UC as InterpretMovieQueryUseCase
    participant I as AiMovieQueryInterpreter (data)
    participant G as AiJsonGenerator (core:ai)
    participant F as GeminiJsonGenerator (core:ai-firebase)
    participant D as GetMoviesByFiltersUseCase

    VM->>UC: invoke("suspense dos anos 90 bem avaliado")
    UC->>I: interpret(description, genres)
    I->>G: generate(prompt, AiSchema)
    G->>F: Gemini, saída estruturada, temperature 0
    F-->>I: JSON
    I-->>UC: Understood(MovieFilters) após validação
    UC-->>VM: QueryInterpretation
    VM->>D: discover/movie com os filtros
    Note over VM: NotUnderstood ou Unavailable → busca por título
```

## Fluxo: erros

```mermaid
flowchart LR
    Ex["HttpException / IOException"] --> Safe["safeApiCall (core:network)"]
    Safe --> Res["AppResult.Failure(DomainError)"]
    Safe -. inesperado .-> Rep["ErrorReporter.reportIfUnexpected"]
    Res --> UiErr["UiState.Error"]
    UiErr --> Msg["DomainError.messageRes() → strings.xml"]
```

`CancellationException` sempre é relançada. Só `DomainError.Server` e `Unknown` viram não fatais no Crashlytics.

## Por que este desenho

| Escolha | Ganho | Custo aceito |
|---|---|---|
| Módulo por camada e por feature | O compilador e o grafo garantem as fronteiras | Mais módulos (convention plugins reduzem o boilerplate) |
| Dagger puro | Grafo explícito, erros em compilação | `api(...)` extras para o KSP do `:app` |
| Contratos JVM para SDKs | Trocar fornecedor sem tocar features; testes sem SDK | Uma camada de indireção |
| Regras como testes (Konsist + module-graph-assert) | Documentação executável; agentes e pessoas recebem feedback imediato | Testes a manter quando uma ADR muda |
