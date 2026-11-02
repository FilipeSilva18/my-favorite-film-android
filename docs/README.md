# Documentação

Organizada pelo framework [Diátaxis](https://diataxis.fr): cada documento tem um único propósito. Escrita para pessoas e para agentes de IA; nenhum documento depende de uma ferramenta específica.

| Tipo | Para quê | Documentos |
|---|---|---|
| **Tutorial** | Aprender fazendo, do zero | [Onboarding](tutorials/onboarding.md) |
| **How-to** | Resolver uma tarefa concreta | [Nova feature](how-to/add-feature.md) · [Novo endpoint da TMDB](how-to/add-endpoint.md) · [Novo bloco SDUI](how-to/add-sdui-block.md) · [Novo evento de analytics](how-to/add-analytics-event.md) · [Escrever testes](how-to/write-tests.md) · [Registrar uma ADR](how-to/write-adr.md) · [Usar os agentes de IA](how-to/use-ai-agents.md) |
| **Referência** | Consultar fatos | [Módulos](reference/modules.md) · [Comandos](reference/commands.md) · [Regras de arquitetura](reference/architecture-rules.md) · [Convenções de código](reference/conventions.md) |
| **Explicação** | Entender o porquê | [Arquitetura em diagramas](explanation/architecture.md) · [IA de agentes no repositório](explanation/ai-agents.md) · [ADRs](decisions/README.md) |

Documentos fora desta pasta:

- [`AGENTS.md`](../AGENTS.md) na raiz e em `app/`, `core/`, `feature/`, `build-logic/`, `architecture-test/`: instruções curtas que os agentes leem sozinhos.
- [`ARCHITECTURE.md`](../ARCHITECTURE.md): documento de arquitetura completo, com contratos e trechos de código.
- [`feature/README.md`](../feature/README.md): checklist de nova feature.
- [`.agents/skills/`](../.agents/skills): skills de agente no padrão aberto Agent Skills.

## Padrões adotados

| Padrão | Onde | Por quê |
|---|---|---|
| [AGENTS.md](https://agents.md) | `AGENTS.md` aninhados | Formato aberto mantido pela Agentic AI Foundation (Linux Foundation), lido por Android Studio (Gemini), Codex, Copilot, Cursor, Gemini CLI e outros |
| [Agent Skills](https://agentskills.io) | `.agents/skills/<nome>/SKILL.md` | Padrão aberto de instruções sob demanda; `.agents/skills` é o diretório lido pelo Android Studio e pelo Codex |
| [MADR 4](https://adr.github.io/madr/) | `docs/decisions/` | Formato enxuto e difundido de ADR, com seção de confirmação |
| [Diátaxis](https://diataxis.fr) | `docs/` | Separa tutorial, how-to, referência e explicação |
| [Mermaid](https://mermaid.js.org) | Diagramas | Renderiza no GitHub e no Android Studio, versionável como texto |

## Manutenção

- Código e documentação divergentes: o código é a fonte da verdade; corrija a documentação no mesmo PR.
- `./scripts/check-docs.py` valida links relativos e o frontmatter das skills; roda no CI.
- Agente responsável: skill [`docs-keeper`](../.agents/skills/docs-keeper/SKILL.md).
