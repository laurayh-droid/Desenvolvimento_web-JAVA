# Módulo de Agendamento — appointment-service

Este documento descreve as funcionalidades expostas pelo **appointment-service** (agendamento) e fornece um guia para criação do banco de dados MySQL (tabelas e atributos) compatível com as entidades atuais do módulo.

---

## 1) Visão geral do módulo

O módulo gerencia:
- **Cadastro de pacientes** (CRUD básico)
- **Agendamento de consultas**
- **Consulta de horários disponíveis**
- **Cancelamento de consulta** com motivo e senha
- **Agendamento de retorno**
- **Registro de prontuário**

Os dados são persistidos via JPA (MySQL) e o módulo expõe endpoints REST sob o prefixo:

- `/api/v1`

---

## 2) Endpoints (API)

### 2.1 Pacientes

#### Cadastrar paciente
- **POST** `/api/v1/pacientes`
- Entrada: `CriarPacienteRequest`
- Saída: `RespostaPaciente`

#### Listar pacientes
- **GET** `/api/v1/pacientes`
- Saída: lista de `RespostaPaciente`

#### Buscar paciente por ID
- **GET** `/api/v1/pacientes/{id}`
- Saída: `RespostaPaciente`

#### Atualizar paciente
- **PUT** `/api/v1/pacientes/{id}`
- Entrada: `AtualizarPacienteRequest`
- Saída: `RespostaPaciente`

---

### 2.2 Consultas (Agendamento)

#### Agendar consulta
- **POST** `/api/v1/consultas`
- Entrada: `CriarAgendamentoRequest`
- Saída: `RespostaAgendamento`

**Regras principais**:
- paciente precisa existir
- evita conflito de agenda para o **mesmo médico** no mesmo horário
- utiliza o status `AGENDADO`

#### Listar consultas de um paciente
- **GET** `/api/v1/consultas/paciente/{pacienteId}`
- Saída: lista de `RespostaAgendamento`

A listagem **exclui** consultas com status `CANCELADO`.

---

### 2.3 Disponibilidade

#### Encontrar horários disponíveis para o médico
- **GET** `/api/v1/consultas/medico/disponibilidade?medicoId=...&inicio=...&fim=...`
- Saída: lista de `RespostaAgendamento` com status `AGENDADO`

**Como funciona**:
- percorre o intervalo `[inicio, fim]` por passos de 1 hora
- para cada slot, verifica se existe algum agendamento para o **mesmo médico** naquele horário
- slots ocupados por status diferentes de cancelados/retorno cancelado não são retornados

---

### 2.4 Cancelamento

#### Cancelar consulta
- **POST** `/api/v1/consultas/{id}/cancelar`
- Entrada: `CancelarAgendamentoRequest`
- Saída: `RespostaAgendamento`

**Regra principal**:
- se já estiver `CANCELADO`, retorna erro
- salva `motivoCancelamento` e `senhaCancelamentoHash`
- altera status para `CANCELADO`
- consultas canceladas não entram nos conflitos futuros (por status)

---

### 2.5 Retorno e prontuário

#### Agendar retorno
- **POST** `/api/v1/consultas/{id}/retorno`
- Entrada: `AgendarRetornoRequest`
- Saída: `RespostaAgendamento`

**Regras principais**:
- não permite retorno para consulta `CANCELADO`
- evita conflito de horário de retorno para o **mesmo médico**
- status final: `RETORNO_AGENDADO`

#### Registrar prontuário
- **PUT** `/api/v1/consultas/{id}/prontuario`
- Entrada: body simples `String` (prontuário)
- Saída: `RespostaAgendamento`

---

## 3) Modelo de dados MySQL (tabelas e atributos)

As entidades do módulo indicam que existem duas tabelas principais:
- `pacientes`
- `agendamentos`

> Observação: nomes e tipos abaixo são derivados das anotações `@Entity` / `@Table` / `@Column`.

---

### 3.1 Tabela `pacientes`

**Campos (mapeamento dos atributos da entidade `Paciente`)**:

- `id` (PK, auto increment)
- `nome_completo` (varchar(200), NOT NULL)
- `rg` (varchar(30), NOT NULL, UNIQUE)
- `cpf` (varchar(30), NOT NULL, UNIQUE)
- `data_nascimento` (datetime, NOT NULL)
- `genero` (varchar(10), NOT NULL) — enum `Gender`

- `telefone` (varchar(30), NULL)
- `telefone_fixo` (varchar(30), NULL)
- `telefone_celular` (varchar(30), NULL)

- `has_seguro` (boolean, NOT NULL)
- `nome_empresa_seguro` (varchar(200), NULL)

- `rua` (varchar(200), NULL)
- `numero` (varchar(20), NULL)
- `complemento` (varchar(100), NULL)
- `bairro` (varchar(100), NULL)
- `cidade` (varchar(100), NULL)
- `estado` (varchar(2), NULL)
- `cep` (varchar(20), NULL)

- `criado_em` (datetime, NOT NULL, updatable=false)
- `atualizado_em` (datetime, NULL)

---

### 3.2 Tabela `agendamentos`

**Campos (mapeamento dos atributos da entidade `Agendamento`)**:

- `id` (PK, auto increment)
- `paciente_id` (bigint, NOT NULL)
- `agendado_em` (datetime, NOT NULL)
- `medico_id` (bigint, NOT NULL)
- `status` (varchar(30), NOT NULL) — enum `StatusAgendamento`
- `motivo_cancelamento` (varchar(500), NULL)
- `senha_cancelamento_hash` (varchar(255), NULL)
- `agendamento_retorno_em` (datetime, NULL)
- `prontuario` (TEXT, NULL)
- `criado_em` (datetime, NOT NULL, updatable=false)
- `atualizado_em` (datetime, NULL)

---

## 4) SQL exemplo (base)

Abaixo um exemplo de DDL inicial (ajuste engine/charset conforme padrão do projeto).

```sql
CREATE TABLE pacientes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome_completo VARCHAR(200) NOT NULL,
  rg VARCHAR(30) NOT NULL UNIQUE,
  cpf VARCHAR(30) NOT NULL UNIQUE,
  data_nascimento DATETIME NOT NULL,
  genero VARCHAR(10) NOT NULL,
  telefone VARCHAR(30) NULL,
  telefone_fixo VARCHAR(30) NULL,
  telefone_celular VARCHAR(30) NULL,
  has_seguro BOOLEAN NOT NULL,
  nome_empresa_seguro VARCHAR(200) NULL,
  rua VARCHAR(200) NULL,
  numero VARCHAR(20) NULL,
  complemento VARCHAR(100) NULL,
  bairro VARCHAR(100) NULL,
  cidade VARCHAR(100) NULL,
  estado VARCHAR(2) NULL,
  cep VARCHAR(20) NULL,
  criado_em DATETIME NOT NULL,
  atualizado_em DATETIME NULL
);

CREATE TABLE agendamentos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  paciente_id BIGINT NOT NULL,
  agendado_em DATETIME NOT NULL,
  medico_id BIGINT NOT NULL,
  status VARCHAR(30) NOT NULL,
  motivo_cancelamento VARCHAR(500) NULL,
  senha_cancelamento_hash VARCHAR(255) NULL,
  agendamento_retorno_em DATETIME NULL,
  prontuario TEXT NULL,
  criado_em DATETIME NOT NULL,
  atualizado_em DATETIME NULL
);
```

---

## 5) Checklist para o banco estar “pronto”

1. Tabelas `pacientes` e `agendamentos` criadas.
2. Colunas do enum `StatusAgendamento` armazenadas como `VARCHAR`.
3. Constraints de unicidade para `pacientes.rg` e `pacientes.cpf`.

---

## 6) Referência rápida (equivalência de campos)

- **Cadastro de paciente**: `CriarPacienteRequest` -> `Paciente` -> `pacientes`
- **Agendamento**: `CriarAgendamentoRequest` -> `Agendamento` -> `agendamentos`
- **Cancelamento**: `CancelarAgendamentoRequest` atualiza `status/motivo_cancelamento/senha_cancelamento_hash`
- **Retorno**: `AgendarRetornoRequest` atualiza `agendamento_retorno_em` e `status`
- **Prontuário**: body `String` atualiza `prontuario`

