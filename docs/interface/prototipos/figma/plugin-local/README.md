# Aplicação manual no Figma sem chamadas MCP

**Registro histórico v0.2.** Este pacote não foi instalado/executado e não inclui a revisão v0.3. Ver [fontes atuais e retomada](../../README.md) e [plano Figma v0.3](../plano-revisao-v03.json). Não executar scripts de criação inicial nem aplicar o pacote sem inspeção do arquivo atual.

**Preparado em 08/11/2026; não instalado nem executado.** É um plugin de desenvolvimento local para aplicar os scripts revisáveis aos 84 frames existentes. Não é plugin generativo, não foi publicado e não mudou o estado do arquivo naquela rodada: as conexões ainda estavam pendentes em 08/11. A aplicação posterior foi realizada por outro meio, conforme [registro nativo v0.5](../revisao-v05/README.md).

A leitura MCP respondeu em 08/11, mas a chamada de gravação voltou a receber bloqueio Starter. Esta alternativa permite executar o trabalho pelo editor autorizado quando houver uma sessão Figma com edição e suporte a plugins locais. Sua disponibilidade nessa sessão não foi verificada aqui.

O pacote confere arquivo, página, nomes exatos dos 84 frames e existência dos controles antes de mutar. Depois aplica ajustes e conexões, confere os destinos e oferece relatório JSON com IDs e três prévias. Não modifica requisitos, cria categorias, instala dependências, publica conteúdo ou acessa a rede. O acesso de rede foi configurado como `none`, conforme o [manifesto oficial de plugins Figma](https://developers.figma.com/docs/plugins/manifest/).

## Preparar e executar

1. No ambiente Figma com suporte a plugins de desenvolvimento, criar um plugin local para obter um `manifest.json` com **ID gerado pelo Figma**. Não usar o texto de exemplo como ID; a documentação oficial define a origem do identificador.
2. Empacotar com esse manifesto:

```bash
python3 docs/interface/prototipos/figma/plugin-local/preparar_plugin.py \
  --manifesto-figma /caminho/do/manifest.json \
  --saida /tmp/pdm-etapa5-plugin
```

3. Importar `/tmp/pdm-etapa5-plugin/manifest.json` como plugin de desenvolvimento. Abrir o [arquivo autoral da Etapa 5](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36), executar o plugin e escolher **Inspecionar sem alterar**. Se houver divergência, parar e revisar o arquivo.
4. Com a inspeção correta, usar **Aplicar ajustes e conexões**. Baixar o relatório JSON. Se ocorrer erro, o relatório registra as fases concluídas; inspecionar o estado antes de repetir.
5. Revisar as prévias e testar o modo de apresentação: seleção, dez pares AHP, revisão, TOPSIS, salvar, retomar e as três ações da DEC-025. Campos e julgamentos continuam ilustrativos. Testar condicionais/overlays no runtime real antes de declarar o Figma navegável e aprovado.

Sem ID real, `preparar_plugin.py` gera apenas `code.js`, `ui.html` e `manifest.template.json`, para revisão. **Não inventa um ID nem declara o pacote pronto para instalação.** A fonte gerada foi verificada sintaticamente; a execução no Figma permanece pendente. O arquivo de especificação v0.1 preserva os 84 frames nativos, separadamente das projeções locais v0.2 de BEV/seleção mista.

Os scripts ainda exigem revisão visual de títulos, botões estreitos, cores M3 e amostras ampliadas; o relatório de destinos não substitui navegação prática. Falhas simuladas adicionais e a expansão local v0.2 não são automaticamente importadas por este pacote.
