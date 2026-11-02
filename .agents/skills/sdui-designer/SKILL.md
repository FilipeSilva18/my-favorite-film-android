---
name: sdui-designer
description: Extends the generic Server-Driven UI vocabulary of the MyFavoriteFilms Android app - SduiComponent model in core common, SduiParser in core network, SduiComponentView renderer in core ui, the simulated backend layout service and their tests. Use when adding or changing a server-driven block type, its JSON contract, or the movie detail server-driven sections.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# SDUI designer

Você evolui o vocabulário de blocos server-driven (ADR-0014).

## Entradas

- Nome do tipo (`snake_case`) e o JSON de exemplo.
- Onde vai aparecer primeiro (hoje, o detalhe do filme).

## Passos

Siga [add-sdui-block.md](../../../docs/how-to/add-sdui-block.md):

1. Modelo em `core/common/.../sdui/SduiComponent.kt`.
2. Constante `TYPE_*` e ramo no `toComponent()` do `SduiParser`.
3. Ramo no `SduiComponentView` (`core/ui/.../sdui/SduiRenderer.kt`).
4. Geração no `MovieDetailLayoutService`, se a tela já usa.
5. Testes: `SduiParserTest`, `SduiRendererTest`, screenshot.
6. Tabela de tipos da seção 7.10 do `ARCHITECTURE.md`.

## Regras do contrato

- Tipo desconhecido é ignorado; nunca lance exceção no parser.
- Campo obrigatório ausente descarta o bloco; item inválido de lista é descartado; lista vazia descarta o bloco.
- Textos chegam prontos para exibição; o app não formata conteúdo do servidor.
- Ícones são chaves semânticas; chave desconhecida = sem ícone.
- Modelo é JVM puro (`core:common`).

## Verificar

```bash
./scripts/verify-module.sh :core:network
./scripts/verify-module.sh :core:ui
./scripts/verify-module.sh :feature:moviedetail:data
```
