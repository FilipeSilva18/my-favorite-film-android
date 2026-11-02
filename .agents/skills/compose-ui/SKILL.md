---
name: compose-ui
description: Builds and changes Jetpack Compose screens and components in the MyFavoriteFilms Android app - Screen/Content split, UiState, ViewModel with StateFlow, type-safe navigation, string resources in pt-BR and en, previews, Robolectric UI tests and Roborazzi screenshots. Use when creating or editing a screen, a composable, a ViewModel, navigation, animations or visual design system components.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Compose UI

You own the `presentation` layer and `core:ui` / `core:designsystem`.

## Before creating any component

Look in `core:designsystem` (`MoviesTheme`, `MoviePoster`, `SearchField`, `LabelChips`) and `core:ui` (`LoadingContent`, `ErrorContent`, `EmptyContent`, `MovieCard`, `MovieGrid`, skeletons, `sduiItems`). A component used by two features goes to the core (ADR-0015).

## Steps for a screen

1. `<Screen>UiState`: `sealed interface` with `Loading`, `Success(...)`, `Error(DomainError)` (or `Flow<PagingData>` for lists).
2. `<Screen>ViewModel`: `@Inject constructor` with only use cases (+ `Analytics`, `SavedStateHandle`); exposes a `StateFlow` with `stateIn(WhileSubscribed(5_000))`; events through `onXxx()`; never exposes `MutableStateFlow`.
3. `<Screen>Screen`: `daggerViewModel()`, `collectAsStateWithLifecycle()`, `TrackScreenView(...)`, passes everything down to `Content`.
4. `<Screen>Content`: no ViewModel; receives state and lambdas; `Modifier` parameter; private previews ending in `Preview` for each state.
5. Strings in `values/strings.xml` (pt-BR) and `values-en/strings.xml`, including `contentDescription`.
6. `<Name>Ui` UI models and mappers in `presentation/model`.
7. Navigation in `presentation/navigation/`: `@Serializable` route, `NavGraphBuilder.<screen>Screen()`, `NavController.navigateTo<Screen>()`.
8. Dagger: `@Binds @IntoMap @ViewModelKey` in `<Feature>PresentationModule`.

## Rules

- No literal text in a `@Composable` (Konsist `UiStringRulesTest`).
- Insets: tab screens apply top and horizontal insets; the bottom bar handles the bottom.
- Animations with Compose APIs (`AnimatedVisibility`, `animateItem`, `animate*AsState`); tests run with animations disabled.
- Material 3 and theme colors; no hardcoded colors outside the design system.

## Tests

- `<Screen>ContentTest` (Robolectric, `@Config(sdk = [36])`): every state and every callback; texts through `context.getString`.
- `<Screen>ScreenshotTest` (Roborazzi): light and dark, `dynamicColor = false`, `pt-rBR`.
- `<Screen>ViewModelTest` with Turbine and fakes.

## Verify

```bash
./scripts/verify-module.sh :feature:<x>:presentation
./scripts/record-screenshots.sh :feature:<x>:presentation
```

Review the changed PNGs before committing.
