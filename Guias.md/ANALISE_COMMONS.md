# Análise de Classes para Módulo Commons

## Resumo Executivo

De acordo com a arquitetura de **microsserviços** descrita no projeto e suas exigências (3 módulos: Administrativo, Agendamento e Atendimento), o módulo `commons` deve conter **todas as classes reutilizáveis entre múltiplos serviços**, evitando duplicação de código.

Após análise do módulo `appointment-service`, identifiquei que **muitas classes já existem duplicadas em ambos os módulos**. Este documento fornece uma recomendação clara sobre qual deve ser a estrutura final.

---

## Status Atual (Problema)

Atualmente existe **duplicação de código** entre `appointment-service` e `commons`:

| Componente | Appointment | Commons | Status |
|---|---|---|---|
| **DTOs** | ✓ | ✓ | **DUPLICADO** |
| **Enums** | ✓ | ✓ | **DUPLICADO** |
| **Exceptions** | ✓ | ✓ | **DUPLICADO** |
| **ApiResponse** | ✗ | ✓ | OK |
| **GlobalExceptionHandler** | ✗ | ✓ | OK |

---

## Classes que DEVEM estar em Commons

Com base nas exigências do projeto (3 módulos: Administrativo, Agendamento, Atendimento), as seguintes classes são **reutilizáveis por múltiplos módulos** e **DEVEM permanecer ou ser movidas para** `commons`:

### 1. **DTOs (Data Transfer Objects)** — Contratos de Comunicação

Estes DTOs representam as **respostas e requisições padrão** da API que serão usados por **todos os três módulos**:

#### ✅ Devem estar em Commons:

```
commons/src/main/java/com/imepac/commons/dto/
├── RespostaPaciente.java          ← Resposta de paciente (Agendam. + Atendimento)
├── RespostaAgendamento.java       ← Resposta de agendamento (Agendam. + Atendimento)
├── CriarPacienteRequest.java      ← Criar paciente (Administrativo + Agendamento)
├── AtualizarPacienteRequest.java  ← Atualizar paciente (Administrativo)
├── CriarAgendamentoRequest.java   ← Criar agendamento (Agendamento)
├── CancelarAgendamentoRequest.java ← Cancelar agendamento (Agendamento)
└── AgendarRetornoRequest.java     ← Agendar retorno (Agendamento + Atendimento)
```

**Justificativa:** Os módulos Administrativo, Agendamento e Atendimento precisarão compartilhar:
- Dados do paciente (RespostaPaciente)
- Dados de agendamento (RespostaAgendamento)
- Estruturas de requisição

Evita repetição e garante **consistência de contrato** entre microsserviços.

---

### 2. **Enums** — Valores Fixos e Enumerações

Estes enums definem **estados e classificações** usados por múltiplos módulos:

#### ✅ Devem estar em Commons:

```
commons/src/main/java/com/imepac/commons/enums/
├── StatusAgendamento.java   ← Estados: AGENDADO, CANCELADO, REALIZADO, etc.
├── Gender.java              ← Gênero: MASCULINO, FEMININO, OUTRO
└── AppointmentStatus.java   ← Alias para StatusAgendamento (redundante?)
```

**Justificativa:**
- `Gender`: Será usado em qualquer módulo que tenha entidade de pessoa
- `StatusAgendamento`: Será usado por Agendamento + Atendimento
- Garantem **valores válidos consistentes** em todo o sistema

---

### 3. **Exceções** — Tratamento de Erros Padronizado

As exceções definem os **erros específicos do domínio** que podem ocorrer em múltiplos módulos:

#### ✅ Devem estar em Commons:

```
commons/src/main/java/com/imepac/commons/exception/
├── EntityNotFoundException.java       ← Entidade não encontrada (base)
├── BusinessException.java             ← Erro de lógica de negócio (base)
├── PacienteNaoEncontradoException.java ← Paciente não existe (herdada)
├── AgendamentoNaoEncontradoException.java ← Agendamento não existe (herdada)
├── FeignIntegrationException.java     ← Erro na integração entre serviços
```

**Justificativa:**
- Exceções **base** (`EntityNotFoundException`, `BusinessException`) são usadas por todos os módulos
- Exceções **específicas** são usadas por múltiplos módulos:
  - `PacienteNaoEncontradoException`: Agendamento + Atendimento
  - `AgendamentoNaoEncontradoException`: Agendamento + Atendimento
  - `FeignIntegrationException`: Comunicação entre microsserviços

---

### 4. **Infraestrutura Compartilhada** — Componentes Transversais

#### ✅ Devem estar em Commons:

```
commons/src/main/java/com/imepac/commons/
├── response/
│   └── ApiResponse.java              ← Envelope padrão de resposta HTTP
├── handler/
│   └── GlobalExceptionHandler.java   ← Tratamento centralizado de erros
├── config/
│   └── CommonsAutoConfiguration.java ← Configuração automática do Spring
└── util/
    └── DateUtils.java                ← Utilitários de data/hora reutilizáveis
```

**Justificativa:**
- `ApiResponse`: **Todos** os microsserviços retornam respostas padronizadas
- `GlobalExceptionHandler`: **Configuração única** para tratamento de erros
- `CommonsAutoConfiguration`: Permite que modules herdarem configurações via Spring Auto-configuration
- `DateUtils`: Funções de manipulação de datas usadas por múltiplos módulos

---

## Classes que devem estar em Appointment-Service

As seguintes classes são **específicas do módulo de agendamento** e **NÃO devem ir para commons**:

### ❌ Ficar em appointment-service:

```
appointment-service/src/main/java/com/imepac/appointment/
├── entity/
│   ├── Paciente.java              ← Entidade JPA (específica do BD local)
│   └── Agendamento.java           ← Entidade JPA (específica do BD local)
├── repository/
│   ├── PacienteRepository.java     ← Spring Data Repository
│   └── AgendamentoRepository.java  ← Spring Data Repository
├── service/
│   ├── AgendamentoService.java     ← Interface de negócio (appointment)
│   └── impl/
│       └── AgendamentoServiceImpl.java ← Implementação de negócio
├── controller/
│   └── AgendamentoController.java  ← Endpoints REST específicos
├── converter/
│   └── AgendamentoConversor.java   ← Conversor Entity ↔ DTO
├── config/
│   ├── FeignConfig.java            ← Configuração de chamadas Feign
│   └── SwaggerConfig.java          ← Documentação OpenAPI
└── exception/
    ├── PacienteNaoEncontradoException.java (especialização - pode ficar aqui ou ir para commons)
    └── AgendamentoNaoEncontradoException.java (especialização - pode ficar aqui ou ir para commons)
```

**Justificativa:**
- **Entidades & Repositories**: Específicas do banco de dados local do appointment-service
- **Services**: Lógica de negócio específica do agendamento
- **Controllers**: Endpoints da API do agendamento
- **Converters**: Transformação Entity ↔ DTO específica dessa entidade
- **Configs**: Configurações específicas do serviço

---

## Recomendação de Ação

### Passo 1: Validar que Commons já contém as classes necessárias

Verifique se `commons` já possui:

```bash
commons/src/main/java/com/imepac/commons/
├── dto/          ← ✓ Contém todos os DTOs
├── enums/        ← ✓ Contém Gender, StatusAgendamento
├── exception/    ← ✓ Contém exceções base
├── response/     ← ✓ Contém ApiResponse
├── handler/      ← ✓ Contém GlobalExceptionHandler
└── config/       ← ✓ Contém CommonsAutoConfiguration
```

### Passo 2: Remover Duplicatas do appointment-service

Se `commons` já contém as classes listadas acima, **remova** as seguintes pastas/arquivos do `appointment-service`:

```
appointment-service/src/main/java/com/imepac/appointment/
├── dto/              ← ❌ REMOVER (usar commons)
├── enums/            ← ❌ REMOVER (usar commons)
├── exception/        ← ❌ REMOVER (usar commons) [opcionalmente, manter se especializar]
```

### Passo 3: Atualizar Imports no appointment-service

Altere todos os imports de:
```java
import com.imepac.appointment.dto.*;
import com.imepac.appointment.enums.*;
import com.imepac.appointment.exception.*;
```

Para:
```java
import com.imepac.commons.dto.*;
import com.imepac.commons.enums.*;
import com.imepac.commons.exception.*;
```

### Passo 4: Verificar Dependências

O `pom.xml` do `appointment-service` já declara dependência em `commons`:
```xml
<dependency>
    <groupId>com.imepac</groupId>
    <artifactId>commons</artifactId>
</dependency>
```

✓ Isso está correto.

---

## Estrutura Recomendada Final

```
order-system/
├── commons/                        ← Módulo compartilhado
│   └── src/main/java/com/imepac/commons/
│       ├── dto/                   ← Todos os DTOs (RespostaPaciente, etc.)
│       ├── enums/                 ← Gender, StatusAgendamento
│       ├── exception/             ← EntityNotFoundException, PacienteNaoEncontradoException, etc.
│       ├── response/              ← ApiResponse
│       ├── handler/               ← GlobalExceptionHandler
│       ├── config/                ← CommonsAutoConfiguration
│       └── util/                  ← DateUtils
│
├── appointment-service/           ← Módulo de Agendamento (sem duplicatas)
│   └── src/main/java/com/imepac/appointment/
│       ├── entity/               ← Paciente, Agendamento (entidades JPA)
│       ├── repository/           ← PacienteRepository, AgendamentoRepository
│       ├── service/              ← AgendamentoService, impl
│       ├── controller/           ← AgendamentoController
│       ├── converter/            ← AgendamentoConversor
│       └── config/               ← FeignConfig, SwaggerConfig
│
├── administrative-service/        ← Módulo Administrativo (a criar)
│   └── src/main/java/com/imepac/administrative/
│       ├── entity/               ← Funcionário, Usuário, Especialidade, Médico, Convênio
│       ├── repository/           
│       ├── service/              
│       ├── controller/           
│       ├── converter/            
│       └── config/               
│
└── attendance-service/           ← Módulo Atendimento (a criar)
    └── src/main/java/com/imepac/attendance/
        ├── entity/               ← Prontuário, Atendimento, Receita, Exame
        ├── repository/           
        ├── service/              
        ├── controller/           
        ├── converter/            
        └── config/               
```

---

## Benefícios desta Abordagem

✅ **DRY (Don't Repeat Yourself):** Evita duplicação de código  
✅ **Consistência:** Todos os módulos usam as mesmas DTOs e exceções  
✅ **Manutenibilidade:** Alterações em contratos afetam um único lugar  
✅ **Escalabilidade:** Novos módulos herdam a estrutura do commons  
✅ **Integração:** Microsserviços comunicam-se com tipos compartilhados  

---

## Próximos Passos

1. **Refatored do appointment-service** para remover duplicatas e usar imports de `commons`
2. **Criar o módulo administrative-service** seguindo a mesma estrutura
3. **Criar o módulo attendance-service** seguindo a mesma estrutura
4. **Documentar as APIs** de cada serviço no README.md
