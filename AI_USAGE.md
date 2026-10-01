# AI_USAGE.md

## 📌 Ferramentas e Modelos de IA Utilizados
Durante o desenvolvimento desta API foram utilizadas ferramentas de Inteligência Artificial para acelerar e estruturar o trabalho:

- **Microsoft Copilot** (assistente de IA utilizado em todo o processo).
- **ChatGPT / Claude Code / Cursor**: referências de agentes de IA que poderiam ser utilizados em cenários semelhantes, mas neste projeto o foco foi no Copilot.
- **Mockito + JUnit**: não são IA, mas foram usados em conjunto com sugestões da IA para geração de testes automatizados.

---

## 🛠️ Estruturação do Trabalho com IA
O trabalho foi conduzido de forma incremental, em etapas claras:

1. **Spec-Driven Development**:  
   - Antes de pedir código, foi elaborado um plano (spec) descrevendo entidades (`Secretaria`, `Responsavel`, `Projeto`), regras de negócio e indicadores.  
   - A IA foi usada para gerar implementações a partir dessa spec.

2. **Iterações com prompts**:  
   - Cada etapa foi guiada por prompts específicos, como:  
     - *"Criar classe de testes para secretaria"*  
     - *"Validar se a secretaria tem algum vínculo com responsável"*  
     - *"Verificar se o responsável não está na tabela projeto_responsavel"*  
     - *"Etapa 3 - Indicadores e Resumos (Opcional)"*  
     - *"Crie as classes completas IndicadorGraphQLController.IndicadorController"*  
     - *"Etapa 4 - Uso de IA no desenvolvimento (Obrigatório)"*

3. **Correções e ajustes**:  
   - A cada sugestão da IA, o código foi revisado e ajustado para atender às regras de negócio e eliminar warnings do compilador.

---

## ⚖️ Decisão de Correção/Discordância
Houve momentos em que foi necessário **corrigir ou rejeitar sugestões da IA**:

- **Exemplo**:  
  A IA sugeriu usar diretamente `Projeto::getStatus` em `Collectors.groupingBy`.  
  O compilador gerou um *warning* de *null type safety*.  
  **Decisão**: corrigir o código adicionando `.filter(p -> p.getStatus() != null)` para evitar valores nulos.  
  Isso garantiu robustez e eliminou o aviso.

---

## 📖 Prompt Representativo
Um trecho de prompt que representa bem o processo de trabalho com IA foi:

> *"Etapa 3 - Indicadores e Resumos (Opcional)  
> • Endpoints REST  
> • Operações no GraphQL (diferencial)  
> • Regras:  
> ◦ Média de dias de atraso por status;  
> ◦ Quantidade de Projetos por status;  
> ◦ Formule novos endpoints e operações, pois será um diferencial.  
> • Testes cobrindo casos de sucesso e erros."*

Esse prompt mostra como a especificação foi usada para guiar a IA na geração de código, endpoints e testes.

---

## 🎯 Conclusão
O uso de IA neste projeto foi **estratégico e incremental**:
- Auxiliou na geração inicial de código e testes.
- Foi ajustado com correções manuais para garantir qualidade.
- Seguiu uma abordagem **spec-driven**, onde primeiro se definia o plano e depois se pedia a implementação.
- A documentação e os testes refletem esse processo colaborativo entre desenvolvedor e IA.

