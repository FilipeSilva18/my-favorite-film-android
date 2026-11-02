---
name: docs-keeper
description: Keeps the MyFavoriteFilms documentation in sync with the code - AGENTS.md files, ARCHITECTURE.md, docs folder organized by Diataxis, MADR architecture decision records, module reference, commands and agent skills. Use after any change to modules, rules, conventions, commands or features, when asked to document something, or when documentation and code disagree.
metadata:
  project: MyFavoriteFilms
  version: "1.0"
---

# Docs keeper

Você mantém a documentação correta. O código é a fonte da verdade.

## Onde cada informação vive

| Mudou | Atualize |
|---|---|
| Módulo novo ou removido | `docs/reference/modules.md`, seção 4 do `ARCHITECTURE.md`, `AGENTS.md` da pasta, diagrama em `docs/explanation/architecture.md` |
| Feature nova | Seção 1 e seção própria do `ARCHITECTURE.md`, `feature/AGENTS.md` |
| Decisão nova | `docs/decisions/NNNN-*.md`, índice em `docs/decisions/README.md`, tabela da seção 2 do `ARCHITECTURE.md` |
| Regra Konsist ou de grafo | `docs/reference/architecture-rules.md`, `AGENTS.md` afetado |
| Comando ou script | `docs/reference/commands.md`, `AGENTS.md` da raiz |
| Convenção | `docs/reference/conventions.md`, seção 14 do `ARCHITECTURE.md` |
| Procedimento recorrente | Guia em `docs/how-to/` e, se for tarefa de agente, skill em `.agents/skills/` |

## Regras de escrita

- Português, frases curtas, termos técnicos corretos (sem analogias).
- Cada documento tem um tipo Diátaxis: tutorial, how-to, referência ou explicação. Não misture.
- `AGENTS.md` curto (menos de 150 linhas): aponte para `docs/` em vez de repetir.
- `SKILL.md`: frontmatter `name` igual ao nome da pasta, `description` em inglês dizendo o que faz e quando usar (até 1024 caracteres), corpo com menos de 500 linhas.
- ADR aceita não é reescrita; crie outra e marque `superseded by`.
- Nenhuma referência a uma ferramenta de IA específica como requisito.
- Diagramas em Mermaid.
- Nenhum segredo, token ou conteúdo de `google-services.json` em exemplos.

## Verificar

```bash
./scripts/check-docs.py
```

Confira também, por amostragem, que nomes de classes, arquivos e comandos citados existem no código.
