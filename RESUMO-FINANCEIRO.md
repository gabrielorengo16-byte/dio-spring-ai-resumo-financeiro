# Melhoria: resumo financeiro

Nova rota `GET /transactions/summary`, sem necessidade de chamar a API da OpenAI.

Resposta ilustrativa:

```json
{
  "totalGasto": 160.00,
  "quantidadeTransacoes": 3,
  "mediaPorTransacao": 53.33,
  "totalPorCategoria": {"GROCERIES": 110.00, "PHARMA": 50.00}
}
```

O campo `amount` das transações é armazenado em **centavos**. O resumo converte os valores para reais.

## Executar no PowerShell

Abra o projeto no VS Code e, no terminal da pasta raiz, execute:

```powershell
$env:DB_PASSWORD = "app"
docker compose up -d
.\gradlew.bat clean build
.\gradlew.bat bootRun
```

A configuração original também requer `OPENAI_API_KEY` para recursos de IA. Configure sua chave como variável de ambiente apenas se for usar esses recursos; nunca a envie para o GitHub.

Com o servidor rodando, abra outro terminal:

```powershell
Invoke-RestMethod http://localhost:8080/transactions/summary | ConvertTo-Json -Depth 5
```

Se a tabela estiver vazia, o resumo retornará zeros. O projeto preserva as rotas originais.
