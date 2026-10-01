# HELP.md

## 📌 Introdução
Este documento fornece informações úteis para desenvolvedores e usuários que desejam configurar, executar e contribuir com o projeto **Kanban API**.  
A API foi construída em **Java (Spring Boot)** e utiliza **Maven** como gerenciador de dependências.

---

## 🚀 Getting Started

### Pré-requisitos
- **Java 21+**
- **Maven 3.9+**
- Banco de dados configurado (ex.: PostgreSQL ou H2 para testes)

### Executando a aplicação
```bash
mvn spring-boot:run
```
---
### A aplicação será iniciada em:

http://localhost:8080

---
## 📂 Estrutura do Projeto

- **controllers/ → Endpoints REST e GraphQL.**

- **services/ → Regras de negócio e cálculos de indicadores.**

- **repositories/ → Interfaces JPA para persistência.**

- **models/ → Entidades do domínio (Projeto, Responsavel, Secretaria).**

- **tests/ → Testes unitários com JUnit e Mockito.**

---
## 📊 Funcionalidades Principais

- **CRUD de Secretarias, Responsáveis e Projetos.**

- **Regras de negócio:**
  - E-mail único para responsáveis e secretarias.
  - Bloqueio de exclusão se houver vínculos.

- **Indicadores:**
  - Projetos por status.
  - Média de dias de atraso.
  - Percentual concluído.
  - Tempo médio de execução.

- **GraphQL:**
  - Queries e mutations para CRUD.
  - Queries e mutations para indicadores.

## 🧪 Testes

### Ferramentas utilizadas
- **JUnit 5**
- **Mockito**

### Casos cobertos
- Sucesso e erro em operações CRUD.
- Validações de regras de negócio.
- Indicadores com cenários de dados nulos ou inexistentes.
