# Adicionar um bloco Server-Driven UI

Quando o "servidor" precisa exibir um tipo de bloco que o app ainda não conhece. Contexto: ADR-0014 e ARCHITECTURE.md seção 7.10. Skill: [`sdui-designer`](../../.agents/skills/sdui-designer/SKILL.md).

## Antes de começar

- O bloco é genérico (serve para qualquer tela)? Se for específico de uma feature, provavelmente não é SDUI.
- Dá para resolver com um bloco existente (`section_title`, `key_value_list`, `chip_group`, `text`, `link`, `divider`, `person_carousel`, `video_carousel`)?
- Textos chegam prontos do servidor. O app não formata nem traduz conteúdo de bloco.

## Passos

1. **Modelo** — `core/common/.../sdui/SduiComponent.kt`: novo `data class` (ou `data object`) que implementa `SduiComponent`. Campos opcionais como `String?`; itens de lista em `data class` próprias.
2. **Parser** — `core/network/.../sdui/SduiParser.kt`:
   - constante `TYPE_<NOME>` em `snake_case`;
   - ramo no `when` de `toComponent()`;
   - campo obrigatório ausente → `null` (o bloco é descartado); item inválido de lista → descartado; lista vazia após filtrar → bloco descartado.
3. **Renderer** — `core/ui/.../sdui/SduiRenderer.kt`: ramo no `when` de `SduiComponentView`. Composable `private` próprio se tiver mais de algumas linhas. `contentDescription` e textos fixos (se houver) vêm de `strings.xml` do `core:ui`.
4. **Backend simulado** — se uma tela já deve usar o bloco, gere-o no serviço de layout da feature (hoje `MovieDetailLayoutService` em `feature:moviedetail:data/sdui`). Rótulos vêm de `MovieDetailLayoutStrings`.
5. **Testes:**
   - `SduiParserTest`: JSON válido, campo obrigatório ausente, itens inválidos, lista vazia.
   - `SduiRendererTest`: o bloco aparece e callbacks (ex.: abrir URL) disparam.
   - `CoreUiScreenshotTest` (ou o screenshot da tela): claro e escuro; `./scripts/record-screenshots.sh :core:ui`.
   - `MovieDetailLayoutServiceTest`, se o backend simulado mudou.
6. **Documentação** — linha nova na tabela de tipos da seção 7.10 do `ARCHITECTURE.md`.

## Verificar

```bash
./scripts/verify-module.sh :core:network
./scripts/verify-module.sh :core:ui
```

## Erros comuns

- Lançar exceção no parser para tipo desconhecido. O contrato é ignorar: versões antigas do app precisam sobreviver a blocos novos.
- Colocar o modelo em `core:ui` ou na feature. O modelo é JVM (`core:common`) para o domain poder carregá-lo.
- Formatar datas ou números no renderer. Isso é papel do servidor.
