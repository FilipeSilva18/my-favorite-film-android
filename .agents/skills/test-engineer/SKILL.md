---
name: test-engineer
description: Writes and fixes tests in the MyFavoriteFilms Android app across all layers - use case and ViewModel tests with fakes, Turbine and paging asSnapshot, MockWebServer contract tests, Room tests with Robolectric, Compose UI tests, Roborazzi screenshots, Dagger graph and instrumented tests. Use when adding test coverage, a test fails, Konsist reports a missing test, or screenshots need re-recording.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Test engineer

Você garante que cada mudança tem os testes certos na camada certa.

## Passos

1. Identifique as classes novas ou alteradas e a camada de cada uma.
2. Consulte a tabela de [write-tests.md](../../../docs/how-to/write-tests.md) e o teste de referência indicado.
3. Escreva primeiro o caso feliz, depois erros e bordas (lista vazia, campo nulo, HTTP 401/404/500, sem rede, cancelamento).
4. Fakes: reutilize os de `core:testing` e `testFixtures`. Fake novo de contrato do core vai para `core:testing`.
5. Rode o módulo; corrija falhas lendo o relatório em `build/reports/tests/`.
6. Mudança visual intencional: `./scripts/record-screenshots.sh <módulo>` e revise cada PNG alterado.

## Regras

- Nunca mocks. Nunca rede real.
- Nomes JVM em inglês entre crases descrevendo comportamento; instrumentados em camelCase.
- Robolectric com `@RunWith(AndroidJUnit4::class)` e `@Config(sdk = [36])`.
- Strings via `context.getString(R.string.x)`.
- Paging em fakes: `Pager` com `PagingSource` simples (não `PagingData.from` estático para `asSnapshot`).
- Não altere código de produção para facilitar o teste sem avisar; se o código é difícil de testar, aponte o problema de design.
- Não apague nem relaxe asserções para o teste passar.

## Pronto quando

- [ ] Todo `UseCase`, `UseCaseImpl` e `ViewModel` tem `<Nome>Test`.
- [ ] Telas novas têm `ContentTest` e `ScreenshotTest`.
- [ ] ViewModel novo está no `DependencyGraphTest`.
- [ ] `./gradlew unitTest` passa.

## Verificar

```bash
./scripts/verify-module.sh <módulo>
./gradlew unitTest
```
