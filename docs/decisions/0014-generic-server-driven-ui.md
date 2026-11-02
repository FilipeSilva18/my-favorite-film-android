---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0014 — Server-Driven UI genérico no core

## Contexto e problema

O detalhe do filme tem blocos cuja ordem e conteúdo o servidor decide, e outras telas podem querer o mesmo.

## Opções consideradas

* Modelo em `core:common`, parser em `core:network`, renderer em `core:ui`
* SDUI dentro da feature

## Decisão

Opção escolhida: SDUI genérico no core; features só produzem e consomem `List<SduiComponent>`, porque qualquer tela reutiliza parser e renderer; tipos desconhecidos são ignorados.

### Consequências

* Bom: Backend evolui antes do app sem quebrar versões antigas
* Ruim: Vocabulário de blocos precisa de governança (guia `add-sdui-block`)

### Confirmação

`SduiParserTest`, `SduiRendererTest`.
