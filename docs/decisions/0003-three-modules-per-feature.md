---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0003 — Três módulos Gradle por feature

## Contexto e problema

Fronteiras entre camadas em pacotes dependem de disciplina. Queremos que o compilador impeça a tela de tocar em Retrofit ou Room.

## Opções consideradas

* `domain`, `data` e `presentation` como módulos separados
* Modelo Now in Android: feature só com UI e camadas em `core`
* Um módulo por feature com camadas em pacotes

## Decisão

Opção escolhida: Cada feature tem até três módulos: `domain`, `data`, `presentation`, porque o grafo de módulos garante as fronteiras e habilita compilação incremental por camada.

### Consequências

* Bom: Uma feature não importa o que não declarou
* Bom: Build paralelo e compile avoidance
* Ruim: Mais módulos e mais `build.gradle.kts` (mitigado pelos convention plugins)

### Confirmação

Estrutura em `settings.gradle.kts`; module-graph-assert proíbe `presentation -X> data`.
