# Adicionar um evento de analytics

Contexto: ADR-0016 e ARCHITECTURE.md seção 7.11.

## Passos

1. **Evento** em `core/analytics/.../AnalyticsEvent.kt`:
   - nome em `AnalyticsEvents` (`snake_case`, até 40 caracteres);
   - parâmetros em `AnalyticsParams` (`snake_case`), reutilizando os existentes (`MOVIE_ID`, `SOURCE`, `SEARCH_TERM`);
   - função fábrica de topo que devolve `AnalyticsEvent(name, params)`, no estilo de `searchEvent(...)` e `aiSearchEvent(...)`;
   - valores fechados viram `enum` com `value` (como `MovieSource`).
2. **Disparo** no ViewModel, com `Analytics` injetado no construtor:
   ```kotlin
   analytics.track(movieSharedEvent(movieId, MovieSource.DETAIL))
   ```
   Telas: `TrackScreenView(ScreenNames.X)` (`core:ui`) no topo do `*Screen`; nome novo em `ScreenNames`.
3. **Teste** no `<ViewModel>Test` com `TestAnalytics` (`core:testing`): afirme o evento e os parâmetros depois da ação.
4. **Documentação:** linha na tabela "Eventos" da seção 7.11 do `ARCHITECTURE.md`.

## Regras

- Nunca importe `com.google.firebase.*` numa feature. O Konsist falha.
- Nunca envie dados pessoais (e-mail, nome) em parâmetros.
- Erro inesperado não é evento: use `ErrorReporter.reportIfUnexpected` no `data`.
- `track` é assíncrono e serializado; não espere o envio nem dependa de retorno.

## Verificar

Debug: Logcat filtrado por `Analytics`. Firebase: DebugView com `adb shell setprop debug.firebase.analytics.app com.faas.myfavoritefilms`.
