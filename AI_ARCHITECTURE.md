# AI_ARCHITECTURE.md

## 📌 Contexto
O sistema Kanban já possui uma API em Java (Spring Boot) com CRUD, regras de negócio, indicadores e resumos.  
Nesta etapa opcional, exploramos como uma **camada de IA** poderia ser integrada para enriquecer o domínio, oferecendo recomendações inteligentes e suporte contextual.

---

## 🛠️ Proposta de Arquitetura

### Objetivo
Criar uma **camada de agentes de IA** que:
- Auxilie usuários na tomada de decisão (ex.: recomendar responsáveis para projetos).
- Gere insights a partir de dados históricos (ex.: prever atrasos).
- Exponha endpoints REST/GraphQL que consultem modelos de linguagem (LLMs) de forma segura.

---

## 📊 Diagrama de Arquitetura da Camada de IA/RAG

```text
+-------------------+        +-------------------+
|   Frontend Kanban | <----> |   API Backend     |
+-------------------+        +-------------------+
            |
            v
+-------------------+
|   Camada de IA    |
|       / RAG       |
+-------------------+
            |
+------------------+------------------+
|                                     |
+---------------+                     +---------------+
|   Vetor DB    |                     |   LLM Provider|
| (Projetos,    |                     | (Copilot,     |
| Responsáveis) |                     | OpenAI, etc.) |
+---------------+                     +---------------+

---

### Contrato de Endpoint (exemplo REST)

- `POST /api/ia/recomendacao-responsavel`
  - **Entrada**:
    ```json
    {
      "projetoId": "uuid",
      "criterios": ["experiencia", "disponibilidade"]
    }
    ```
  - **Saída**:
    ```json
    {
      "responsavelSugerido": {
        "id": "uuid",
        "nome": "Maria Silva",
        "cargo": "Gestora"
      },
      "justificativa": "Responsável já atuou em 3 projetos similares e está disponível."
    }
    ```

---

### Estratégia de Contexto
- **RAG (Retrieval-Augmented Generation)**:
  - Dados de projetos e responsáveis são indexados em um banco vetorial.
  - O LLM recebe contexto relevante (ex.: histórico de atrasos, carga atual de trabalho).
  - A resposta é gerada com base em dados reais + inferência do modelo.

---

### Tratamento de Erros/Timeout
- **Timeouts**: se o provedor de LLM não responder em até 5s, retornar fallback com recomendação baseada apenas em regras de negócio locais.
- **Erros**: logar falhas e retornar mensagem clara ao usuário:
  ```json
  { "erro": "Serviço de IA indisponível. Tente novamente mais tarde." }



---

## ⚖️ Trade-offs

### Prós
- Recomendação contextualizada e inteligente.
- Diferencial competitivo (IA aplicada ao Kanban).
- Flexibilidade via REST e GraphQL.

### Contras
- Custo de chamadas a LLMs externos.
- Latência maior em consultas complexas.
- Necessidade de tratamento robusto de erros e segurança.

---

## 📖 Spec-Driven Development (Exemplo)

**Spec**: Endpoint de recomendação de responsável para projeto.  
- **Entrada**: `projetoId`, critérios de seleção.  
- **Saída**: responsável sugerido + justificativa.  
- **Regras**:
  - IA consulta histórico de projetos similares.
  - IA considera disponibilidade atual.
  - Se IA falhar, aplicar regra local: escolher responsável com menos projetos ativos.

**Derivação da Implementação**:
1. Criar endpoint REST `/api/ia/recomendacao-responsavel`.
2. Integrar camada RAG para buscar contexto.
3. Chamar LLM com prompt estruturado.
4. Validar resposta e aplicar fallback se necessário.

---

## 🎯 Conclusão
Resumo dos benefícios da camada de IA:
- Enriquecimento da experiência do usuário.
- Recomendações inteligentes e contextualizadas.
- Arquitetura robusta com fallback e validação.
- Diferencial competitivo para o sistema Kanban.

👉 Esse `AI_ARCHITECTURE.md` pode ser incluído no repositório como parte da **Etapa 5 (Diferencial)**.  
Quer que eu também monte um **exemplo de prompt RAG** (como seria enviado ao LLM com dados de contexto) para deixar a documentação ainda mais prática?