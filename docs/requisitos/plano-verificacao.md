# Plano inicial de verificação dos requisitos

**Versão:** 1.0 — plano documental consolidado conforme decisões aprovadas; execução futura.
**Execução:** nenhum cenário executado; aplicativo ainda não implementado.

[Requisitos](README.md) · [Rastreabilidade](rastreabilidade.md) · [Casos de uso](casos-de-uso/README.md)

Este plano organiza a cobertura já aprovada nos RNFs e os fluxos dos RFs. Os identificadores VT representam conjuntos de verificações, não novos requisitos nem quantidade obrigatória de testes. Ferramentas, massas concretas e implementação dos testes serão definidas posteriormente. As modalidades abaixo respeitam os mínimos do RNF10; não estabelecem percentual de cobertura.

## Cenários e resultados esperados

| ID | Cenários / preparação | Resultado esperado | Modalidade prevista |
|---|---|---|---|
| VT01 | Catálogo com duas categorias, produto completo/incompleto; consulta remota, cache, vazio e falhas | Produtos da categoria correta; unidades e referências coerentes; cache identificado; erro distinto de consulta vazia; nenhuma data inventada | Integração e interface; inspeção documentada dos estados |
| VT02 | Cadastrar, reiniciar, consultar, editar e excluir produto pessoal; reutilizar em duas avaliações | Produto persiste localmente; categoria imutável; cancelamento preserva cadastro; snapshots anteriores permanecem iguais | Persistência automatizada e roteiro de interface |
| VT03 | Uma alternativa; repetição do mesmo par origem + ID; IDs iguais com origens diferentes; registros distintos com mesmo nome/modelo; categoria incompatível; campos ausentes, não finitos, notas fora de 1–10 e coluna com norma zero | Validação rejeita repetir a mesma identidade e aceita identidades distintas sem deduplicação por nome/modelo, inclusive na mesma origem; aplica metadados DEC-033 e bloqueia cálculo inválido. Limites adicionais serão definidos na modelagem/preparação dos dados; rascunho incompleto permanece permitido | Validações automatizadas |
| VT04 | Criar nas duas categorias, selecionar produtos oficiais/pessoais e ajustar valores em avaliações diferentes | Critérios corretos e categoria fixa; valores próprios por avaliação; catálogos intactos; menos de dois produtos não processa | Persistência/validação automatizada e interface |
| VT05 | Responder parcialmente e concluir dez pares; voltar e alterar julgamento | Progresso correto, diagonal 1, reciprocidade e nenhuma resposta presumida; alteração invalida consistência | Validação automatizada e interface documentada |
| VT06 | Matrizes conhecidas, matriz toda 1, inválidas e RC no limite | Pesos/indicadores conforme DEC-018 e tolerância DEC-029; comparar RC diretamente; teste da regra com 0,10 aceita e 0,10004 rejeita | Motores/validação automatizados; separar teste da regra de limite do teste de cálculo de RC |
| VT07 | TOPSIS conhecido com custo/benefício; grupos empatados; todas alternativas idênticas | Cálculo correto, posições competitivas; resultado sem diferenciação reconhecido e distinguido de normalização inválida, sem coeficiente fictício | Motores automatizados com referências independentes |
| VT08 | Salvar rascunho parcial e avaliação processada com ranking ou sem diferenciação; reiniciar e retomar; alterar definições no catálogo; falhar durante gravação com vários registros | Dados e metadados da última versão salva restaurados; sem gravação parcial nem falso sucesso; rascunho não vira concluída; processamento válido sem diferenciação reabre como “Concluída — sem diferenciação”, sem C*, posição ou vencedor fictício | Persistência automatizada e roteiro de interface |
| VT09 | Editar concluída; descartar; salvar sem recalcular; recalcular e salvar; provocar falha; sair com mudanças | Descarte restaura versão salva; salvamento das entradas invalida resultado antigo atomicamente; novo resultado só persiste por ação explícita | Persistência automatizada; aviso Salvar/Descartar/Continuar automatizado na interface |
| VT10 | Excluir rascunho/concluída, cancelar confirmação e provocar falha; excluir produto referenciado | Exclusão de avaliação atômica; catálogos intactos; cancelamento não muda dados; snapshots sobrevivem à remoção de produto | Persistência automatizada e confirmação na interface documentada |
| VT11 | Conectividade desativada com dados completos, cache parcial e sem cache | Operações locais disponíveis conforme dados; avaliação completa não depende de nova leitura remota; indisponibilidade explicada | Integração e roteiro offline documentado |
| VT12 | Cliente sem autenticação tenta ler/criar/alterar/excluir nas coleções oficiais, inclusive fora da UI | Leitura pública permitida; todas as escritas do cliente negadas; sem credenciais administrativas no app/repositório | Regras Firestore automatizadas e inspeção de configuração |
| VT13 | Revisar tráfego, permissões e backup/transferência nas versões Android suportadas | Dados pessoais não enviados ao Firestore; backup/transferência excluídos conforme DEC-030; aviso de perda de dados presente | Inspeção e verificação Android documentadas; versões ainda a definir |
| VT14 | Avaliações válidas com 2, 10 e 50 produtos, cinco critérios; aquecimento; 100 medições por cenário | Pelo menos 95 de 100 medições de cada cenário ≤500 ms; escopo medido exatamente conforme DEC-026; interação responsiva | Medição em build de produção em dispositivo físico documentado, além da verificação de interação |
| VT15 | Protocolo de três participantes; larguras 360/600 dp em retrato com fonte padrão/200%; TalkBack | Pelo menos 2/3 concluem cada tarefa sem ajuda de navegação; sem corte/controles inacessíveis; alvo ≥48×48 dp; estados não dependem só de cor | Usabilidade, visual, interação e TalkBack conforme RNF01/RNF02 |
| VT16 | Revisar dependências, executar suíte, seguir README com outro integrante e inspecionar Git/Projects | Motores isolados; cobertura mínima RNF10; evidências de execução; documentação reproduzível; tags de entregas reais; fluxo DEC-031 e tarefas atualizadas | Revisão técnica/documental e inspeção do processo |
| VT17 | Exportar avaliação salva: vencedor único, empate e ausência de diferenciação; offline; edição pendente com as três opções; recálculo necessário; falhas de salvar/gerar/compartilhar e cancelamento | PDF corresponde à versão salva escolhida e contém os oito itens do RF27 sem matrizes intermediárias; não mistura edição pendente com resultado antigo; não exporta rascunho; preserva avaliação em falhas e cancelamentos; geração/abertura não é apresentada como envio confirmado | Verificação documental do PDF e roteiro de integração/interface; execução ainda não realizada |

## Verificação da exportação

Conferir o PDF contra a versão persistida, inclusive após mudanças nos catálogos. Verificar conteúdo em múltiplas páginas quando necessário, com valores e unidades legíveis, sem perda de linhas de produtos. A geração offline é independente de eventual rede exigida pelo destino escolhido. O limite de 500 ms dos motores não se aplica ao PDF. Estes roteiros organizam a verificação do RF27 e não criam novas metas quantitativas ou obrigação de automatização além do RNF10.

## Casos numéricos que precisam de atenção

- Em VT06, valores escolhidos para testar a regra de bloqueio não devem ser confundidos com matrizes cuja RC tenha sido previamente comprovada. Fixtures para motores exigem referência independente de precisão suficiente.
- Em VT07, uma cadeia ilustrativa de C* é `0,8`, `0,7999999993` e `0,7999999986`: os dois primeiros pertencem ao mesmo grupo, mas o terceiro não, pois a referência é o maior do grupo. A fixture final deve verificar valores internos e a tolerância de 10⁻⁹.
- Testar a numeração competitiva em grupo intermediário (1,2,2,4), não apenas empate no primeiro lugar.
- Ausência de diferenciação com colunas normalizáveis e C* indefinido é distinta de coluna de norma zero, cuja normalização já é inválida. Nenhum dos casos permite inventar coeficientes.
- A tolerância combinada DEC-029 serve à comparação com referências de teste; não amplia RC≤0,10 nem substitui a regra de empate DEC-027.

## Fluxos mínimos de interface

Conforme RNF10, automatizar pelo menos uma avaliação completa até o resultado e o aviso de alterações não salvas. A cobertura deve também contemplar revisão de AHP inconsistente, retomada de rascunho e confirmação de exclusão, que podem ser verificadas manualmente com registro. Não se declara que toda linha VT será automatizada na interface.

## Registro de execução futuro

Para cada execução, registrar cenário/variante, requisito, versão ou commit do aplicativo, data, dispositivo/Android, build/configuração relevante, dados de entrada e referência esperada, resultado observado, situação e evidência ou defeito encontrado. Para desempenho, guardar as 100 medições por cenário e documentar o aquecimento adotado, além do resumo de aprovação. Não fixamos aqui nova quantidade obrigatória de aquecimento.

Situações: **não executado**, **aprovado**, **reprovado** ou **bloqueado**, com motivo quando aplicável. Atualmente todos os conjuntos estão **não executados**. Não confundir aprovação documental com resultados de testes.

## Condições de preparação

Dispositivo físico, versões Android, dados de referência de alta precisão, limites de entrada, conteúdo do catálogo, configuração de testes e infraestrutura ainda precisam ser definidos na etapa apropriada. Problemas que bloqueiem fluxos aprovados, comprometam dados, produzam cálculos incorretos ou permitam escrita indevida devem ser corrigidos antes da entrega, conforme RNF10.
