---
name: compose-ui
description: Builds and changes Jetpack Compose screens and components in the MyFavoriteFilms Android app - Screen/Content split, UiState, ViewModel with StateFlow, type-safe navigation, string resources in pt-BR and en, previews, Robolectric UI tests and Roborazzi screenshots. Use when creating or editing a screen, a composable, a ViewModel, navigation, animations or visual design system components.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Compose UI

Você cuida da camada `presentation` e de `core:ui` / `core:designsystem`.

## Antes de criar qualquer componente

Procure em `core:designsystem` (`MoviesTheme`, `MoviePoster`, `SearchField`, `LabelChips`) e `core:ui` (`LoadingContent`, `ErrorContent`, `EmptyContent`, `MovieCard`, `MovieGrid`, skeletons, `sduiItems`). Componente usado por duas features vai para o core (ADR-0015).

## Passos para uma tela

1. `<Tela>UiState`: `sealed interface` com `Loading`, `Success(...)`, `Error(DomainError)` (ou `Flow<PagingData>` para listas).
2. `<Tela>ViewModel`: `@Inject constructor` só com use cases (+ `Analytics`, `SavedStateHandle`); expõe `StateFlow` com `stateIn(WhileSubscribed(5_000))`; eventos via `onXxx()`; nunca expõe `MutableStateFlow`.
3. `<Tela>Screen`: `daggerViewModel()`, `collectAsStateWithLifecycle()`, `TrackScreenView(...)`, repassa para o `Content`.
4. `<Tela>Content`: sem ViewModel; recebe estado e lambdas; `Modifier` como parâmetro; previews `private` terminados em `Preview` para cada estado.
5. Strings em `values/strings.xml` (pt-BR) e `values-en/strings.xml`, inclusive `contentDescription`.
6. Modelos de UI `<Nome>Ui` e mappers em `presentation/model`.
7. Navegação em `presentation/navigation/`: rota `@Serializable`, `NavGraphBuilder.<tela>Screen()`, `NavController.navigateTo<Tela>()`.
8. Dagger: `@Binds @IntoMap @ViewModelKey` no `<Feature>PresentationModule`.

## Regras

- Nenhum texto literal em `@Composable` (Konsist `UiStringRulesTest`).
- Insets: telas de aba aplicam topo e laterais; a bottom bar cuida do inferior.
- Animações com APIs do Compose (`AnimatedVisibility`, `animateItem`, `animate*AsState`); respeite `disable-animations` nos testes.
- Material 3 e cores do tema; nada de cor fixa fora do design system.

## Testes

- `<Tela>ContentTest` (Robolectric, `@Config(sdk = [36])`): cada estado e cada callback; textos via `context.getString`.
- `<Tela>ScreenshotTest` (Roborazzi): claro e escuro, `dynamicColor = false`, `pt-rBR`.
- `<Tela>ViewModelTest` com Turbine e fakes.

## Verificar

```bash
./scripts/verify-module.sh :feature:<x>:presentation
./scripts/record-screenshots.sh :feature:<x>:presentation
```

Revise os PNGs alterados antes de versionar.
