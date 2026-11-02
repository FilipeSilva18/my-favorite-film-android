# Add an analytics event

Context: ADR-0016 and ARCHITECTURE.md section 7.11.

## Steps

1. **Event** in `core/analytics/.../AnalyticsEvent.kt`:
   - name in `AnalyticsEvents` (`snake_case`, up to 40 characters);
   - parameters in `AnalyticsParams` (`snake_case`), reusing existing ones (`MOVIE_ID`, `SOURCE`, `SEARCH_TERM`);
   - a top-level factory function returning `AnalyticsEvent(name, params)`, in the style of `searchEvent(...)` and `aiSearchEvent(...)`;
   - closed sets of values become an `enum` with a `value` (like `MovieSource`).
2. **Dispatch** from the ViewModel, with `Analytics` injected in the constructor:
   ```kotlin
   analytics.track(movieSharedEvent(movieId, MovieSource.DETAIL))
   ```
   Screens: `TrackScreenView(ScreenNames.X)` (`core:ui`) at the top of the `*Screen`; new name in `ScreenNames`.
3. **Test** in `<ViewModel>Test` with `TestAnalytics` (`core:testing`): assert the event and its parameters after the action.
4. **Documentation:** a row in the "Events" table in section 7.11 of `ARCHITECTURE.md`.

## Rules

- Never import `com.google.firebase.*` in a feature. Konsist fails.
- Never send personal data (email, name) as parameters.
- An unexpected error is not an event: use `ErrorReporter.reportIfUnexpected` in `data`.
- `track` is asynchronous and serialized; don't wait for delivery or depend on a return value.

## Verify

Debug: Logcat filtered by `Analytics`. Firebase: DebugView with `adb shell setprop debug.firebase.analytics.app com.faas.myfavoritefilms`.
