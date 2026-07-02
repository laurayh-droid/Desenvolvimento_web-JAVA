# Resumo do Projeto — Clínica Médica (Order System)

## 📋 Sobre o Projeto

Sistema de gerenciamento para clínica médica desenvolvido em **arquitetura de microsserviços** com Java 17 e Spring Boot 3.5.14. O sistema é composto por 5 módulos independentes:

- **commons** - Módulo compartilhado (entidades, DTOs, exceções)
- **administrative-service** - Gerenciamento de usuários, médicos, convênios e especialidades
- **appointment-service** - Agendamento de consultas
- **attendance-service** - Atendimento médico e prontuários
- **gateway-service** - API Gateway (Spring Cloud Gateway) para roteamento

Cada serviço possui seu próprio banco de dados MySQL e se comunica via HTTP/REST.

*** 

* O gateway **escuta na porta 8080**
* Ele **redireciona** as requisições para os serviços nas portas 8083, 8084 e 8085
* A porta **3306** é do banco de dados MySQL, não tem relação com o gateway

Ou seja, o cliente acessa apenas a porta 8080 do gateway, e internamente o gateway faz o roteamento para os serviços apropriados.

---

## 🛠️ Tecnologias Principais

### Backend

- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.14** - Framework principal
- **Spring MVC** - Construção de APIs REST
- **Spring Data JPA** - Persistência de dados (ORM com Hibernate)
- **Spring Cloud Gateway** - API Gateway para roteamento
- **OpenFeign** - Comunicação entre microsserviços (cliente HTTP declarativo)

### Banco de Dados

- **MySQL** - Banco de dados relacional (database-per-service)

### Utilitários

- **Lombok 1.18.30** - Redução de código boilerplate
- **Bean Validation** - Validação de dados de entrada
- **SpringDoc OpenAPI 2.3.0** - Documentação automática da API (Swagger)
- **Logbook 3.8.0** - Logging de requisições HTTP
- **Maven 4.0.0** - Gerenciamento de build e dependências

### DevOps

- **Docker & Docker Compose** - Containerização
- **Kubernetes** - Orquestração de containers

---

## 🧪 Tecnologias de Teste

### Frameworks de Teste

- **JUnit 5** - Framework de testes unitários
- **Spring Boot Test** - Suporte a testes de integração
- **Mockito** - Mocking de dependências (via spring-boot-starter-test)
- **Testcontainers** - Containers Docker para testes de integração com MySQL

### Tipos de Teste

- **Testes Unitários** - Testam services e converters isoladamente
- **Testes de Integração** - Testam controllers com contexto Spring completo
- **Testes de Repositório** - Testam acesso a dados com banco real (via Testcontainers)
- **Testes de Smoke** - Testes básicos de funcionamento

### Banco de Dados para Testes

- **MySQL via Testcontainers** - Garante que os testes usam o mesmo banco de produção
- **H2** (opcional) - Banco em memória para testes mais rápidos (não usado atualmente)

### Ferramentas de Teste Manual

- **Postman** - Teste manual de endpoints da API
- **Collection do Postman** - `imepac-clinica-medica-system.postman_collection.json`

---

## 🏗️ Arquitetura

**Padrão em Camadas:**

- Controller (API/REST)
- Service (Regras de negócio)
- Repository (Acesso a dados)
- Entity/DTO (Modelos)

**Padrões Aplicados:**

- DTO (Data Transfer Object)
- Repository Pattern
- Service Layer
- Global Exception Handling
- Converter Pattern (Entity ↔ DTO)

O projeto segue princípios de **Clean Code** e **SOLID**, com separação clara de responsabilidades e injeção de dependências via construtor.
