# Relatório da Arquitetura do Projeto Clínica Médica

Este documento detalha a estrutura arquitetural, as tecnologias empregadas e os padrões de testes identificados no projeto **Desenvolvimento_web-JAVA**.

---

## 🏗️ 1. Estrutura da Arquitetura

O sistema é construído utilizando o padrão de **Microsserviços**, orquestrado de forma modular através do Maven (projeto multi-módulo). A arquitetura é desenhada com foco na separação de contextos (*Bounded Contexts*) para uma Clínica Médica, com comunicação facilitada através de um Gateway.

### Componentes Principais:
- **`gateway-service` (Porta 8080)**: Age como API Gateway, roteando requisições dos clientes externos para os serviços de back-end correspondentes.
- **`administrative-service` (Porta 8084)**: Serviço de administração, responsável por gerir funcionários, médicos, especialidades, convênios e configurações da clínica.
- **`appointment-service` (Porta 8083)**: Serviço de agendamentos. Responsável por lidar com a criação de novos pacientes, gestão de disponibilidade e marcação de consultas.
- **`attendance-service` (Porta 8085)**: Serviço de atendimento. Focado no registro de prontuários, triagem e o atendimento médico em si.
- **`commons`**: Um módulo compartilhado que contém configurações transversais, classes utilitárias, DTOs, Handlers de erro e Entidades core do negócio que se repetem (como `Paciente` e `Agendamento`).

**Persistência**: 
O sistema segue o princípio *Database per Service*. Cada microsserviço que demanda persistência possui seu próprio banco de dados independente (`appointment_db`, `administrative_db`, `attendance_db`), garantindo baixo acoplamento de dados.

---

## ⚙️ 2. Tecnologias Utilizadas

A stack tecnológica do projeto é focada no ecossistema Spring atualizado e conteinerização:

- **Linguagem**: Java 17
- **Framework Principal**: Spring Boot 3.5.14
- **Ecossistema Cloud**: Spring Cloud 2025.0.0 (para funcionalidades do Gateway e roteamento)
- **Banco de Dados**: MySQL 8.0
- **Ferramentas de Suporte**:
  - **Lombok** (1.18.30): Redução de código clichê (*boilerplate*).
  - **SpringDoc OpenAPI** (2.3.0): Geração automática de documentação Swagger.
  - **Logbook** (3.8.0): Para log reativo e simplificado de requisições e respostas HTTP.
- **Infraestrutura e Deploy**:
  - **Docker e Docker Compose**: Usados para a conteinerização local da aplicação e serviços dependentes (bancos de dados).
  - **Kubernetes (K8s)**: O projeto possui manifestos prontos na pasta `k8s/` para deploy orquestrado em cluster.

---

## 🧪 3. Tipos de Testes Utilizados

Através da estrutura de código no diretório `src/test/java`, o projeto adota diferentes níveis de teste automatizados baseados na stack do ecossistema Spring Boot (tipicamente **JUnit 5** e **Mockito**):

1. **Testes Unitários de Regra de Negócio**
   - Focados em validar o comportamento interno e as regras de negócio das classes de serviço isoladamente (utilizando *mocks* para os repositórios).
   - Exemplos identificados: `PacienteServiceImplTest.java`, `ConsultaServiceImplTest.java`, `MedicoServiceImplTest.java`, `RegistrarAtendimentoServiceImplTest.java`.

2. **Testes de Fumaça (*Smoke Tests*)**
   - Testes de sanidade rápidos usados para verificar se os fluxos mais vitais não quebram de maneira evidente na subida de contexto do serviço.
   - Exemplo: `AppointmentSmokeTest.java`.

3. **Testes de Contexto da Aplicação (*Application Context Tests*)**
   - Utilizam anotações como `@SpringBootTest` para verificar se os microsserviços inteiros conseguem iniciar sem falhas no carregamento dos *Beans* ou injeções de dependência.
   - Identificados em todos os módulos, garantindo que o módulo seja viável em produção: `AdministrativeServiceApplicationTest.java`, `AttendanceServiceApplicationTest.java`, `GatewayServiceApplicationTest.java`.
