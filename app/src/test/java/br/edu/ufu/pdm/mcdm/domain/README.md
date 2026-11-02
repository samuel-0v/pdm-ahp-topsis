# Validação do motor AHP-TOPSIS

O motor foi isolado de Android e Compose para facilitar validação matemática antes da integração com a interface.

Quando o projeto Gradle Android for inicializado, cobrir pelo menos:
- Matriz recíproca perfeitamente consistente (pesos esperados e RC próxima de zero).
- Matriz com RC > 0,10, confirmando que o processamento deve ser bloqueado pelo fluxo de aplicação.
- Rejeição de matriz não quadrada, não positiva ou não recíproca.
- TOPSIS com critérios de benefício e custo e ranking conhecido.
- Rejeição de dimensões incompatíveis e de coluna sem norma.
- Casos de RI=0 (1–2 critérios), apresentados como RC não definida.

Os exemplos numéricos dos PDFs podem servir de referência, mas devem ser recalculados e conferidos antes de virarem resultados de teste: os documentos usam configurações diferentes e a simulação contém resultados arredondados.
