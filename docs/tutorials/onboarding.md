# Onboarding: from clone to first PR

One afternoon. By the end you will have the app running, the tests passing and a small change made the project's way.

## 1. Environment (15 min)

1. Current Android Studio (Quail or newer) and JDK 21.
2. Clone the repository and open the root folder.
3. Create a TMDB account and generate the **API Read Access Token** (Settings → API).
4. In `local.properties` (already ignored by Git):
   ```properties
   TMDB_ACCESS_TOKEN=your_token
   ```
5. Protect the repository against committing secrets:
   ```bash
   ./scripts/install-git-hooks.sh
   ```
6. Optional: `app/google-services.json` from your own Firebase project, for analytics and AI search. Without it the app works and AI search falls back to title search.

## 2. Run (10 min)

```bash
./gradlew :app:assembleDebug
./scripts/verify.sh --quick
./scripts/verify.sh
```

Run the app on the emulator. Navigate: list → search → detail (look at the cast and trailers, which come from SDUI) → favorite → Favorites tab → Settings.

## 3. Understand (45 min)

Read in this order:

1. [`README.md`](../../README.md) and [`AGENTS.md`](../../AGENTS.md): the rules on one page.
2. [Architecture in diagrams](../explanation/architecture.md).
3. ADRs [0002](../decisions/0002-pure-kotlin-domain.md), [0005](../decisions/0005-no-feature-to-feature-dependency.md), [0017](../decisions/0017-feature-api-module.md) and [0018](../decisions/0018-repositories-depend-on-data-sources.md).
4. Follow a tap through the code: `MoviesScreen` → `MoviesViewModel` → `GetMoviesUseCase` → `MoviesRepository` → `MoviesRepositoryImpl` → `MoviesRemoteDataSource` → `MoviesApi`.
5. Open `MoviesViewModelTest` and `MoviesContentTest` to see how each layer is tested.

## 4. Break things on purpose (15 min)

See the rules at work:

1. In `MoviesViewModel`, inject `MoviesRepository` in the constructor. Run `./scripts/verify.sh --quick`: Konsist fails with `ViewModel depends on use cases, never on repositories`.
2. In `feature/movies/presentation/build.gradle.kts`, add `implementation(projects.feature.movies.data)`. Run `./gradlew assertModuleGraph`: it fails.
3. In a `@Composable`, write `Text("Hello")`. `UiStringRulesTest` fails.

Undo everything.

## 5. First change (1 h)

Add a `movie_shared` analytics event with a share button on the detail screen:

1. Follow [add-analytics-event.md](../how-to/add-analytics-event.md).
2. The button text and `contentDescription` go in `values/strings.xml` and `values-en/strings.xml`.
3. Update `MovieDetailContentTest` and `MovieDetailViewModelTest` and re-record the screenshot: `./scripts/record-screenshots.sh :feature:moviedetail:presentation`.
4. `./scripts/verify.sh`.
5. Open the PR; the template brings the definition of done.

## 6. Next steps

- New feature: [add-feature.md](../how-to/add-feature.md).
- Working with an AI agent: [use-ai-agents.md](../how-to/use-ai-agents.md).
