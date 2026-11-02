# Onboarding: do clone ao primeiro PR

Uma tarde. No fim você terá o app rodando, os testes passando e uma mudança pequena feita no padrão do projeto.

## 1. Ambiente (15 min)

1. Android Studio atual (Quail ou mais novo) e JDK 21.
2. Clone o repositório e abra a pasta raiz.
3. Crie uma conta na TMDB e gere o **API Read Access Token** (Settings → API).
4. Em `local.properties` (já ignorado pelo Git):
   ```properties
   TMDB_ACCESS_TOKEN=seu_token
   ```
5. Proteja o repositório contra commits de segredo:
   ```bash
   ./scripts/install-git-hooks.sh
   ```
6. Opcional: `app/google-services.json` de um projeto Firebase seu, para analytics e busca com IA. Sem ele, o app funciona e a busca com IA cai na busca por título.

## 2. Rodar (10 min)

```bash
./gradlew :app:assembleDebug
./scripts/verify.sh --quick
./scripts/verify.sh
```

Rode o app no emulador. Navegue: lista → busca → detalhe (veja o elenco e os trailers, que vêm de SDUI) → favoritar → aba Favoritos → Ajustes.

## 3. Entender (45 min)

Leia nesta ordem:

1. [`AGENTS.md`](../../AGENTS.md): regras em uma página.
2. [Arquitetura em diagramas](../explanation/architecture.md).
3. ADRs [0002](../decisions/0002-pure-kotlin-domain.md), [0005](../decisions/0005-no-feature-to-feature-dependency.md), [0017](../decisions/0017-feature-api-module.md) e [0018](../decisions/0018-repositories-depend-on-data-sources.md).
4. Siga um clique no código: `MoviesScreen` → `MoviesViewModel` → `GetMoviesUseCase` → `MoviesRepository` → `MoviesRepositoryImpl` → `MoviesRemoteDataSource` → `MoviesApi`.
5. Abra `MoviesViewModelTest` e `MoviesContentTest` para ver como cada camada é testada.

## 4. Quebrar de propósito (15 min)

Veja as regras funcionando:

1. Em `MoviesViewModel`, injete `MoviesRepository` no construtor. Rode `./scripts/verify.sh --quick`: o Konsist falha com `ViewModel depends on use cases, never on repositories`.
2. Em `feature/movies/presentation/build.gradle.kts`, adicione `implementation(projects.feature.movies.data)`. Rode `./gradlew assertModuleGraph`: falha.
3. Num `@Composable`, escreva `Text("Olá")`. O `UiStringRulesTest` falha.

Desfaça tudo.

## 5. Primeira mudança (1 h)

Adicione um evento de analytics `movie_shared` com um botão de compartilhar no detalhe:

1. Siga [add-analytics-event.md](../how-to/add-analytics-event.md).
2. O texto do botão e o `contentDescription` vão em `values/strings.xml` e `values-en/strings.xml`.
3. Atualize `MovieDetailContentTest`, `MovieDetailViewModelTest` e regrave o screenshot: `./scripts/record-screenshots.sh :feature:moviedetail:presentation`.
4. `./scripts/verify.sh`.
5. Abra o PR; o template traz a definição de pronto.

## 6. Próximos passos

- Feature nova: [add-feature.md](../how-to/add-feature.md).
- Trabalhar com um agente de IA: [use-ai-agents.md](../how-to/use-ai-agents.md).
