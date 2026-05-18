# Entidades Compartilhadas em Commons

## 📋 Resumo das Mudanças

Implementei a **segunda abordagem**: entidades compartilhadas em `commons` para evitar duplicação entre módulos. Assim, quando o módulo de **Atendimento** precisar acessar Paciente ou Agendamento, usará as mesmas entidades de **Agendamento**.

---

## ✅ Ações Realizadas

### 1. **Criação de Entidades em Commons**

Foram criadas 2 novas entidades JPA em commons:

```
commons/src/main/java/com/imepac/commons/entity/
├── Paciente.java         ← NOVA
└── Agendamento.java      ← NOVA
```

**Características:**
- Entidades JPA com `@Entity` e mapeamento para o banco de dados
- Usam enums de commons (`Gender`, `StatusAgendamento`)
- Incluem validações e callbacks de auditoria (`@PrePersist`, `@PreUpdate`)
- Totalmente independentes de qualquer módulo específico

### 2. **Atualização de Imports em Appointment-Service**

4 arquivos foram atualizados:

| Arquivo | Mudança |
|---|---|
| `converter/AgendamentoConversor.java` | `com.imepac.appointment.entity.*` → `com.imepac.commons.entity.*` |
| `repository/PacienteRepository.java` | Importar `Paciente` de commons |
| `repository/AgendamentoRepository.java` | Importar `Agendamento` de commons |
| `service/impl/AgendamentoServiceImpl.java` | Importar entidades de commons |

### 3. **Remoção da Pasta Entity**

A pasta `appointment-service/src/main/java/com/imepac/appointment/entity/` foi **removida completamente**.

```
❌ appointment-service/src/main/java/com/imepac/appointment/entity/
   ├── Paciente.java          ← REMOVIDA
   └── Agendamento.java       ← REMOVIDA
```

### 4. **Adição de Dependência JPA em Commons**

O arquivo `commons/pom.xml` foi atualizado:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Isso permite que `commons` contenha entidades JPA que serão usadas por todos os microsserviços.

---

## 📊 Estrutura Final

### Commons (Módulo Compartilhado)

```
commons/
├── src/main/java/com/imepac/commons/
│   ├── entity/
│   │   ├── Paciente.java         ✅ Compartilhada
│   │   └── Agendamento.java      ✅ Compartilhada
│   ├── dto/
│   │   ├── RespostaPaciente.java
│   │   ├── RespostaAgendamento.java
│   │   ├── CriarPacienteRequest.java
│   │   └── ... (mais DTOs)
│   ├── enums/
│   │   ├── Gender.java
│   │   ├── StatusAgendamento.java
│   │   └── AppointmentStatus.java
│   ├── exception/
│   ├── response/
│   ├── handler/
│   ├── config/
│   └── util/
└── pom.xml ← Contém spring-boot-starter-data-jpa
```

### Appointment-Service (Simplificado)

```
appointment-service/
├── src/main/java/com/imepac/appointment/
│   ├── AppointmentServiceApplication.java
│   ├── config/
│   │   ├── FeignConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/
│   │   └── AgendamentoController.java
│   ├── converter/
│   │   └── AgendamentoConversor.java
│   ├── repository/
│   │   ├── PacienteRepository.java     (usa Paciente de commons)
│   │   └── AgendamentoRepository.java  (usa Agendamento de commons)
│   └── service/
│       ├── AgendamentoService.java
│       └── impl/
│           └── AgendamentoServiceImpl.java
└── pom.xml ← Já depende de commons
```

---

## 🔗 Benefícios das Entidades Compartilhadas

✅ **Sem Duplicação**: Paciente e Agendamento existem em um único lugar  
✅ **Consistência de BD**: Todos os módulos veem a mesma estrutura de dados  
✅ **Reutilização**: Novos módulos herdam automaticamente as entidades  
✅ **Manutenibilidade**: Alterações em Paciente/Agendamento afetam todos os serviços  
✅ **Escalabilidade**: Fácil expandir com novas entidades em commons  

---

## 🏗️ Como Funciona em Microsserviços

```
┌──────────────────────────────────┐
│         commons                  │
│  ├── entity/Paciente      ✓      │
│  ├── entity/Agendamento   ✓      │
│  ├── dto/*                ✓      │
│  ├── enums/*              ✓      │
│  └── exception/*          ✓      │
└──────────────────────────────────┘
           ▲                 ▲
           │                 │
    ┌──────┴──┐      ┌──────┴──────┐
    │          │      │             │
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ appointment  │  │ administrative│  │ attendance   │
│   service    │  │   service     │  │  service     │
│              │  │               │  │              │
│ ├─ repository│  │ ├─ repository │  │ ├─ repository│
│ │ (Paciente, │  │ │(Funcionário,│  │ │(Prontuário,│
│ │ Agendamento)  │ │Especialidade) │ │ Atendimento)
│ │ usam       │  │ │ usam       │  │ │ consultam  │
│ │ commons    │  │ │ commons    │  │ │ commons    │
│ └────────────│  │ └────────────│  │ └────────────│
└──────────────┘  └──────────────┘  └──────────────┘
   DB: pacientes,     DB: funcionários,   DB: prontuários,
   agendamentos       especialidades,     atendimentos,
                      médicos,            receitas,
                      convênios           exames
```

---

## 🧪 Validação

### Compilação ✅
```bash
$ mvn clean compile -DskipTests
# Sucesso - sem erros
```

### Empacotamento ✅
```bash
$ mvn clean package -DskipTests
# Sucesso - JARs gerados:
# - commons-1.0.0.jar
# - appointment-service-1.0.0.jar
```

---

## 📝 Arquivos Modificados

| Arquivo | Tipo |
|---|---|
| `commons/src/main/java/com/imepac/commons/entity/Paciente.java` | ✅ CRIADO |
| `commons/src/main/java/com/imepac/commons/entity/Agendamento.java` | ✅ CRIADO |
| `commons/pom.xml` | ✅ Adicionada dependência JPA |
| `appointment-service/converter/AgendamentoConversor.java` | ✅ Import atualizado |
| `appointment-service/repository/PacienteRepository.java` | ✅ Import atualizado |
| `appointment-service/repository/AgendamentoRepository.java` | ✅ Import atualizado |
| `appointment-service/service/impl/AgendamentoServiceImpl.java` | ✅ Import atualizado |
| `appointment-service/src/main/java/com/imepac/appointment/entity/` | ❌ REMOVIDA |

---

## 🚀 Próximos Passos

1. **Criar Administrative-Service** com suas próprias entidades:
   - Funcionário
   - Usuário
   - Especialidade
   - Médico
   - Convênio

2. **Criar Attendance-Service** com suas próprias entidades:
   - Prontuário
   - Atendimento
   - Receita
   - Exame

3. **Estrutura final** seguirá o padrão:
   - Commons: entidades compartilhadas + DTOs + exceções
   - Cada serviço: repositories + services + controllers + converters **específicos de seu domínio**

4. **Testar integração** com todos os três módulos funcionando juntos

---

## 💡 Padrão de Desenvolvimento

Todos os novos módulos devem seguir este padrão:

```
novo-service/
├── src/main/java/com/imepac/novomodulo/
│   ├── NovoServiceApplication.java
│   ├── config/
│   │   └── SwaggerConfig.java
│   ├── controller/
│   ├── converter/
│   ├── repository/          (importa entidades de commons ou cria as suas)
│   └── service/
├── pom.xml                  (depende de commons)
└── target/                  (JAR executável)
```

Todos importam de `commons` as classes compartilhadas (DTOs, exceções, enums, entidades)! ✨
