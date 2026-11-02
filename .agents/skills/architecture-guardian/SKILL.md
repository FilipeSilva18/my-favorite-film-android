---
name: architecture-guardian
description: Reviews changes in the MyFavoriteFilms Android app against its architecture decision records, module graph rules and Konsist rules, and evolves those rules through new ADRs. Use when reviewing a pull request or diff, deciding where new code belongs, a module graph or Konsist check fails, or someone proposes changing an architectural rule.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Architecture guardian

Você revisa mudanças contra as decisões do projeto. Aponta problemas com o arquivo, a regra e a ADR; não reescreve a mudança inteira.

## Fontes

- [Regras executáveis](../../../docs/reference/architecture-rules.md)
- [ADRs](../../../docs/decisions/README.md)
- [Convenções](../../../docs/reference/conventions.md)

## Passos de revisão

1. Rode `./scripts/verify.sh --quick`. Falhas de grafo ou Konsist são bloqueantes.
2. Para cada arquivo alterado, confira o que os testes não pegam:
   - O código está no módulo e na camada certos? Algo usado por duas features deveria subir para o core?
   - Use case com uma responsabilidade? ViewModel com lógica de negócio que deveria estar num use case?
   - Repositório decidindo política (cache, erro, mapeamento) e data source só acessando a fonte?
   - Tipos públicos que deveriam ser `internal`?
   - SDK de terceiro fora de um módulo `-<fornecedor>`?
   - Erros: `AppResult`/`DomainError` na fronteira, `CancellationException` relançada?
   - Comentários explicativos no código (proibido) ou decisão sem ADR?
   - Segredos, `local.properties` ou `google-services.json` no diff?
3. Classifique cada achado: **bloqueante** (viola regra ou ADR), **sugestão** (melhora sem violar), **pergunta**.
4. Para cada achado: arquivo e linha, o que está errado, a regra ou ADR, a correção esperada.

## Mudando uma regra

Uma regra só muda com ADR nova ([write-adr.md](../../../docs/how-to/write-adr.md)). Altere o teste Konsist ou o `moduleGraphAssert`, `docs/reference/architecture-rules.md` e o `AGENTS.md` afetado no mesmo PR. Nunca relaxe uma regra para fazer um build passar.

## Formato da resposta

```
Bloqueantes
- feature/x/presentation/.../XViewModel.kt:12 — recebe XRepository; ADR-0004; injete GetXUseCase.

Sugestões
- ...

Perguntas
- ...
```
