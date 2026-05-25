# Order System — Microsserviços com Spring Boot

> Projeto modular de microsserviços para agendamento, atendimento e administração médica.

## Visão geral

Este repositório contém um sistema de microsserviços Java baseado em Spring Boot e Maven, construído para gerenciar:

- `appointment-service`: agendamento de consultas e pacientes
- `attendance-service`: registros de atendimento e prontuários
- `administrative-service`: funcionalidades administrativas relacionadas à operação do sistema
- `commons`: módulo compartilhado com classes, DTOs e configurações comuns

O projeto usa:

- Java 17
- Spring Boot 3.5.14
- Spring Cloud 2025.0.0
- MySQL 8.0
- Docker / Docker Compose

## Estrutura do projeto

- `pom.xml`: POM pai que define os módulos e dependências comuns
- `commons/`: biblioteca compartilhada entre os módulos
- `administrative-service/`: serviço administrativo
- `appointment-service/`: serviço de agendamento de consultas
- `attendance-service/`: serviço de atendimento médico
- `docker-compose.yml`: orquestração dos serviços e bancos de dados MySQL

## Requisitos

- Java 17
- Maven 3.8+
- Docker
- Docker Compose

## Build e execução

### 1) Compilar todo o projeto

No diretório raiz do repositório:

```bash
mvn clean package -DskipTests
```

Isso gera os JARs dos módulos sob `*/target/`.

### 2) Execução com Docker Compose (recomendado)

Ainda no diretório raiz:

```bash
docker-compose up --build
```

O Compose irá subir:

- `mysql-appointment` (MySQL para appointment-service)
- `appointment-service` na porta `8083`
- `mysql-administrative` (MySQL para administrative-service)
- `administrative-service` na porta `8084`
- `mysql-attendance` (MySQL para attendance-service)
- `attendance-service` na porta `8085`

Para parar e remover containers e volumes:

```bash
docker-compose down -v
```

### 3) Execução local sem Docker

Cada módulo pode ser executado diretamente com Maven após instalar `commons`:

```bash
mvn clean install -pl commons
```

Exemplo para iniciar um módulo:

```bash
mvn spring-boot:run -pl appointment-service
```

> Os módulos esperam conexão com MySQL caso estejam configurados para isso. Verifique `src/main/resources/application.yml` de cada serviço.

## Ports e variáveis do Docker Compose

| Serviço | Porta exposta | Variáveis de ambiente |
|---|---|---|
| `appointment-service` | `8083` | `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` |
| `administrative-service` | `8084` | `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` |
| `attendance-service` | `8085` | `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` |

Os bancos criados pelo Compose são:

- `appointment_db`
- `administrative_db`
- `attendance_db`

## Dockerfiles

Os módulos de serviço usam Dockerfiles locais:

- `appointment-service/Dockerfile`
- `administrative-service/Dockerfile`
- `attendance-service/Dockerfile`

Cada Dockerfile copia o JAR gerado e inicia a aplicação com `java -jar app.jar`.

## Módulos

### commons

Módulo compartilhado com classes utilitárias, DTOs, Exceptions e configurações que são usadas nos demais serviços.

### appointment-service

Serviço de agendamento que gerencia pacientes, consultas e disponibilidade de horários.

### administrative-service

Serviço administrativo para funcionalidades de gestão do sistema.

### attendance-service

Serviço de atendimento médico que registra atendimentos, prontuários e histórico clínico.

## Documentação adicional

Cada módulo possui documentação específica:

- `appointment-service/README_MODULO_AGENDAMENTO.md`
- `attendance-service/README_MODULO_ATENDIMENTO.md`

## Observações

- O projeto usa Maven multi-módulo, então o comando de build deve ser executado na raiz do repositório.
- O `docker-compose.yml` depende dos JARs gerados em `target/` para os serviços Java.
- Se alterar a versão dos serviços ou nomes de JAR, atualize também os `ARG JAR_FILE` nos Dockerfiles correspondentes.
