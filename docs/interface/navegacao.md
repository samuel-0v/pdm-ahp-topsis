# Navegação e fluxo da avaliação

Fluxo funcional de referência, alinhado às DEC-022–DEC-025 e aos RF10–RF26. A categoria fica fixa durante toda a avaliação.

```mermaid
flowchart TD
    CAT[Catálogo] --> CATEG[Selecionar categoria]
    CATEG --> NEW[Criar avaliação para a categoria]
    NEW --> SEL[Selecionar produtos da mesma categoria]
    NEW --> DRAFT[Salvar rascunho manualmente]
    SEL --> DRAFT
    VAL --> DRAFT
    AHP --> DRAFT
    PROG --> DRAFT
    DRAFT --> DRAFTSAVE[Persistir rascunho localmente]
    DRAFTSAVE --> LIST[Avaliações salvas]
    SEL --> VAL[Ajustar valores dos critérios]
    VAL --> AHP[Comparar um par de critérios]
    AHP --> PROG[Atualizar progresso e permitir voltar/revisar]
    PROG --> MORE{Há pares pendentes?}
    MORE -- Sim --> AHP
    MORE -- Não --> CHECK[Matriz completa e recíproca]
    CHECK --> CONS[Calcular pesos e consistência AHP]
    CONS --> CR{RC ≤ 0,10?}
    CR -- Não --> AHP
    CR -- Sim --> SUMMARY[Revisar resumo da avaliação]
    SUMMARY --> TOP[Processar TOPSIS]
    TOP --> DEGEN{Existe diferenciação?}
    DEGEN -- Não: d+ = d- = 0 --> NODIFF[Exibir “Sem diferenciação entre as alternativas”]
    DEGEN -- Sim --> RANK[Ordenar por C*; manter posições empatadas]
    RANK --> RESULT[Consultar ranking, pesos e distâncias]
    NODIFF --> RESULT
    RESULT --> SAVE{Salvar explicitamente?}
    SAVE -- Sim --> PERSIST[Persistir configuração e resultado válido quando existente]
    SAVE -- Não --> DIRTY[Manter alterações pendentes]
    PERSIST --> LIST
    LIST --> EDIT[Retomar ou editar avaliação]
    EDIT --> CHANGE[Modificar dados da avaliação]
    CHANGE --> DIRTY
    DIRTY --> EXIT{Sair com alterações?}
    EXIT -- Salvar --> PERSIST
    EXIT -- Descartar --> LIST
    EXIT -- Continuar editando --> CHANGE
    LIST --> DELETE[Solicitar exclusão]
    DELETE --> CONF{Confirmar?}
    CONF -- Sim --> REMOVE[Excluir avaliação localmente]
    CONF -- Não --> LIST
```

## Regras de navegação

- O usuário não altera a categoria depois da criação da avaliação; para outra categoria, inicia uma nova.
- As comparações AHP aparecem uma por vez, com progresso e retorno para revisão. Alterar uma comparação invalida a validação de consistência anterior.
- Uma matriz AHP incompleta pode permanecer em rascunho, mas não libera TOPSIS.
- RC acima de 0,10 retorna o fluxo para revisão dos julgamentos.
- Alterações não são salvas automaticamente. Ao sair com mudanças pendentes, oferecer **Salvar**, **Descartar alterações** ou **Continuar editando**.
- Processar em memória não salva o resultado; sucesso de salvamento só é informado após persistência local confirmada.
- Empates matemáticos compartilham posição. Se não houver diferenciação matemática, informar o estado sem declarar vencedor.

## Exportação em PDF — DEC-032

A consulta da avaliação processada oferece exportação pelo [UC16](../requisitos/casos-de-uso/05-exportacao.md), conforme [RF27](../requisitos/funcionais/07-exportacao.md). Sem edição pendente, gerar a partir da versão salva elegível e abrir o compartilhamento padrão do Android.

Com alterações não salvas, oferecer **Salvar e exportar**, **Exportar última versão salva** e **Cancelar**. A primeira opção exige resultado atualizado e persistência bem-sucedida; a segunda depende de versão salva processada e mantém a edição pendente. Informar necessidade de recálculo antes de permitir exportar uma edição desatualizada. Cancelamentos e falhas não alteram a avaliação salva. Este complemento textual não constitui protótipo de telas.

Identidade das alternativas segue origem + ID (DEC-034). Processamento válido sem diferenciação pode ser salvo e listado como **Concluída — sem diferenciação** (DEC-035); isso não converte falhas de normalização em resultados válidos.
