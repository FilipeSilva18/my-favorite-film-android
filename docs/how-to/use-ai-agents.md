# Usar agentes de IA neste repositório

Funciona com qualquer assistente de código. Nada aqui depende de um fornecedor.

## O que o agente encontra

| Arquivo | Quando é lido |
|---|---|
| `AGENTS.md` (raiz e por pasta) | Sempre, pelas ferramentas que suportam o formato. Contexto curto: mapa, comandos, regras, definição de pronto |
| `.agents/skills/<nome>/SKILL.md` | Sob demanda, quando a tarefa bate com a `description` da skill |
| `docs/` | Quando uma skill ou o `AGENTS.md` aponta para um guia |

## Skills disponíveis

| Skill | Use para |
|---|---|
| [`feature-builder`](../../.agents/skills/feature-builder/SKILL.md) | Criar uma feature nova de ponta a ponta |
| [`data-integrator`](../../.agents/skills/data-integrator/SKILL.md) | Endpoint da TMDB, DTO, data source, repositório, Room |
| [`compose-ui`](../../.agents/skills/compose-ui/SKILL.md) | Telas, componentes, estados, previews, screenshots |
| [`sdui-designer`](../../.agents/skills/sdui-designer/SKILL.md) | Bloco novo de Server-Driven UI |
| [`test-engineer`](../../.agents/skills/test-engineer/SKILL.md) | Escrever ou corrigir testes em qualquer camada |
| [`architecture-guardian`](../../.agents/skills/architecture-guardian/SKILL.md) | Revisar uma mudança contra as ADRs e regras |
| [`docs-keeper`](../../.agents/skills/docs-keeper/SKILL.md) | Atualizar documentação depois de uma mudança |

## Por ferramenta

| Ferramenta | Como usar |
|---|---|
| Gemini no Android Studio | Lê `AGENTS.md` e `.agents/skills/` sozinho (Android Studio Quail ou mais novo). Invoque uma skill com `@nome` no chat |
| Codex, Copilot, Cursor, Gemini CLI, Junie e outras compatíveis com AGENTS.md | Lêem o `AGENTS.md` sozinhas. Skills: automático nas que suportam Agent Skills; nas outras, peça "siga `.agents/skills/<nome>/SKILL.md`" |
| Ferramenta que lê outro nome de arquivo | Crie um link simbólico local para o `AGENTS.md` com o nome que ela espera, sem versioná-lo, ou referencie o `AGENTS.md` nas instruções dela |
| Chat sem acesso ao repositório | Cole o conteúdo do `AGENTS.md` da raiz e o `SKILL.md` da tarefa no início da conversa |

## Pedindo bem

- Diga o resultado, não os passos: "crie a feature `watchlist` com lista e botão de adicionar, consumindo `account/{id}/watchlist/movies`".
- Aponte a skill quando a ferramenta não escolher sozinha.
- Peça o `./scripts/verify.sh` no fim. Um agente que roda os testes corrige os próprios erros; um que não roda entrega código que não compila.
- Revise o diff como revisaria o de uma pessoa: o build garante as regras, não a qualidade da regra de negócio.

## Segurança

- Rode `./scripts/install-git-hooks.sh`: o hook bloqueia `local.properties`, `google-services.json` e tokens mesmo que um agente tente commitá-los.
- Não cole o token da TMDB nem o `google-services.json` na conversa com o agente.
- Peça confirmação antes de o agente rodar comandos que publicam algo (push, release).
