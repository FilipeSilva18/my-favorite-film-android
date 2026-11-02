---
status: accepted
date: 2026-11-01
decision-makers: Filipe
---

# 0011 — Token da TMDB fora do controle de versão

## Contexto e problema

O token de leitura da TMDB é segredo e não pode aparecer no repositório.

## Opções consideradas

* `local.properties` → `BuildConfig` do `core:network`; secret no CI
* Hardcode no código
* Backend proxy

## Decisão

Opção escolhida: Token em `local.properties` (ignorado) e no secret `TMDB_ACCESS_TOKEN` do CI, exposto via `BuildConfig`, porque mantém o segredo fora do Git com configuração simples.

### Consequências

* Bom: Nenhum segredo versionado
* Ruim: O token vai dentro do APK (aceitável para token de leitura; um proxy seria necessário para publicação séria)

### Confirmação

`.gitignore` e o hook `scripts/git-hooks/pre-commit`.
