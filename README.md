# Início do projeto PDM — AHP-TOPSIS

## Leitura das instruções

Arquivos consultados:
- `TRABALHOS/ProjetoDisciplina_PDM20262v2.pdf` — enunciado detalhado do sistema Android AHP-TOPSIS.
- `TRABALHOS/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf` — explicação dos métodos e simulação Kotlin.
- `PROVAS/p1/chatGPT/chatGPT.md` — anotações de IA sobre aulas; material de apoio, não requisito do projeto.
- Materiais de `MATERIAL-AULA/SLIDES` e `ATIVIDADES` foram inventariados para orientar a implementação.

## Requisitos do professor identificados

- Aplicativo Android em Kotlin, MVVM e Jetpack Compose.
- Comparação multicritério de produtos com AHP para obter pesos e TOPSIS para ordenar alternativas.
- AHP: matriz pareada de critérios, pesos, λmax, IC/CI e RC/CR; bloquear o processamento quando RC > 0,10.
- TOPSIS: normalização vetorial, aplicação dos pesos, ideais positivo e negativo segundo benefício/custo, distâncias euclidianas e coeficiente de proximidade.
- Telas descritas: splash, categorias, produtos por categoria, detalhes, pesos, avaliação dos produtos, resumo/processamento e resultados.
- Navegação estruturada, integração de dados remota por API ou Firebase, e interface intuitiva/responsiva.
- Entrega da Fase 1 indicada no PDF: 03/11/2026 (requisitos, arquitetura, navegação, protótipos e vídeo). Fase 2: 11/12/2026.

## Divergências e pontos a confirmar no grupo/professor

1. O PDF `v2` pede pelo menos duas coleções de produtos, mas descreve também um estudo de caso de notebooks com quatro alternativas e cinco critérios.
2. A tabela de domínios inclui notebooks, smartphones, veículos elétricos e provedores de nuvem; o grupo deve escolher pelo menos duas categorias e definir fonte confiável para seus dados.
3. O PDF detalhado sugere grupo de até seis pessoas; em outra seção fala em oito alunos. Confirmar a regra aplicável.
4. A simulação do PDF AHP+TOPSIS (5 produtos/8 critérios) é ilustrativa e não coincide com o estudo de caso de quatro notebooks/cinco critérios do `v2`.
5. A etapa AHP aparece com dois métodos: média geométrica no material explicativo e normalização das colunas + média das linhas no `v2`. Confirmar qual método será usado/esperado; o motor inicial abaixo usa média geométrica, explicitamente descrita no material complementar.
6. A integração remota é requisito; a tecnologia/API ainda precisa ser escolhida. Evitar inventar dados de mercado como se fossem reais.

## Estrutura inicial criada

- `app/src/main/java/.../domain/AhpTopsis.kt`: motor matemático puro, sem dependência de Android/UI.
- `app/src/test/.../domain/README.md`: cenários recomendados para validação assim que o projeto Gradle Android for inicializado.

## Próximas decisões

1. Confirmar equipe, duas categorias, alternativas e critérios.
2. Confirmar o método AHP e política para matrizes com 1 ou 2 critérios (RC não informativa quando RI=0).
3. Confirmar provedor de dados remoto e estratégia offline/cache.
4. Inicializar o projeto Android/Gradle e ligar o motor ao ViewModel e às telas Compose.

Os nomes de telas, exemplos numéricos e recomendações encontrados no Markdown de aula são contexto. Só os requisitos explícitos nos PDFs do trabalho são tratados como instruções do professor.
