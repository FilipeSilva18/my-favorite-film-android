# feature/ — funcionalidades

Complementa o [AGENTS.md da raiz](../AGENTS.md). Checklist detalhado: [feature/README.md](README.md). Passo a passo: [docs/how-to/add-feature.md](../docs/how-to/add-feature.md).

## Features

| Feature | Módulos | Referência para |
|---|---|---|
| `movies` | `domain`, `data`, `presentation` | Paging 3, busca com debounce, busca com IA, data source remoto |
| `moviedetail` | `domain`, `data`, `presentation` | `SavedStateHandle`, SDUI, tela com estados |
| `favorites` | `api`, `domain`, `data`, `presentation` | Feature consumida por outras (ADR-0017), data source local (Room) |
| `settings` | `presentation` | Feature só de UI sobre regras do `core:domain` |

## Estrutura e plugins

```
feature/<nome>/
├── api/           movies.jvm.library + java-test-fixtures   (só se outra feature consumir)
├── domain/        movies.jvm.library (+ movies.dagger)       model/ repository/ usecase/
├── data/          movies.android.feature.data                datasource/ remote/ mapper/ repository/ di/
└── presentation/  movies.android.feature.presentation        di/ navigation/ model/ <Tela>Screen/ViewModel/UiState
```

## Regras que o build verifica

- Uma feature só importa outra pelo pacote `.api.` e só depende do módulo `:api` dela.
- `presentation` não depende de `data`; `data` não depende de `presentation`.
- `domain` sem Android, Room, Retrofit, Compose ou Dagger (só `javax.inject`).
- Use case em `domain/usecase`, com um único `operator fun invoke` público.
- Repositório: interface no `domain/repository`; `internal` `<Nome>RepositoryImpl` em `data/repository`, que depende de data sources.
- DTO `internal` em `data/remote/dto`; data source `internal` em `data/datasource`.
- ViewModel em `presentation`, termina em `ViewModel`, recebe só use cases, nunca expõe `MutableStateFlow`.
- `@Preview` privado e terminado em `Preview`. Nenhum texto literal em `@Composable`.

## Dagger puro: atenção

`data` e `presentation` declaram `api(projects.feature.<nome>.domain)`. O `AppComponent` é gerado no `:app` e precisa enxergar os tipos do domain. Sem isso o KSP falha com `'...Repository' could not be resolved`.

## Testes mínimos

`<UseCase>Test`, `<ViewModel>Test`, teste de contrato do data com MockWebServer e JSON real em `src/test/resources/tmdb/`, `<Tela>ContentTest`, `<Tela>ScreenshotTest`. Detalhes: [docs/how-to/write-tests.md](../docs/how-to/write-tests.md).
