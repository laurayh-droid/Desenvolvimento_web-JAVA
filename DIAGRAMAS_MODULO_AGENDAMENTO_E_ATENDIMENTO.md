# Diagrama de Classe (Uso) — Agendamento e Atendimento

## Módulo Agendamento (appointment-service)

```mermaid
classDiagram
  %% =========================
  %% Agendamento (appointment)
  %% =========================
  AgendamentoController --> AgendamentoService : usa
  AgendamentoService <|-- AgendamentoServiceImpl
  AgendamentoServiceImpl --> AgendamentoRepository : usa
  AgendamentoServiceImpl --> PacienteRepository : usa
  AgendamentoServiceImpl --> Agendamento : cria/atualiza

  class AgendamentoController {
    +agendarConsulta(CriarAgendamentoRequest)
    +cancelarConsulta(agendamentoId, CancelarAgendamentoRequest)
    +agendarRetorno(agendamentoId, AgendarRetornoRequest)
    +registrarProntuario(agendamentoId, prontuario)
  }

  class AgendamentoService {
    +agendarConsulta(CriarAgendamentoRequest) RespostaAgendamento
    +cancelarConsulta(Long, CancelarAgendamentoRequest) RespostaAgendamento
    +agendarRetorno(Long, AgendarRetornoRequest) RespostaAgendamento
    +registrarProntuario(Long, String) RespostaAgendamento
  }

  class AgendamentoServiceImpl {
    -PacienteRepository pacienteRepository
    -AgendamentoRepository agendamentoRepository
  }

  class AgendamentoRepository {
    +findAllByPacienteId(Long)
    +existsByMedicoIdAndAgendadoEmAndStatusNotIn(...)
    +findAllByPacienteIdAndStatusNot(...)
  }

  class PacienteRepository {
    +findByCpf(String)
    +existsByCpf(String)
    +existsByRg(String)
    +findByRg(String)
  }

  class Agendamento {
    +Long id
    +Long pacienteId
    +LocalDateTime agendadoEm
    +Long medicoId
    +StatusAgendamento status
    +String motivoCancelamento
    +String senhaCancelamentoHash
    +LocalDateTime agendamentoRetornoEm
    +String prontuario
  }
```

## Módulo Atendimento (attendance-service)

```mermaid
classDiagram
  %% =========================
  %% Atendimento (attendance)
  %% =========================
  AtendimentoController --> AtendimentoService : usa
  AtendimentoService <|-- AtendimentoServiceImpl
  AtendimentoServiceImpl --> AtendimentoRepository : usa
  AtendimentoServiceImpl --> Atendimento : cria/atualiza

  class AtendimentoController {
    +registrarAtendimento(CriarAtendimentoRequest)
    +listarAtendimentosPorPaciente(pacienteId)
    +buscarPorAgendamento(agendamentoId)
  }

  class AtendimentoService {
    +registrarAtendimento(CriarAtendimentoRequest) RespostaAtendimento
    +listarAtendimentosPorPaciente(Long) List~RespostaAtendimento~
    +buscarPorAgendamento(Long) RespostaAtendimento
  }

  class AtendimentoServiceImpl {
    -AtendimentoRepository atendimentoRepository
  }

  class AtendimentoRepository {
    +findByAgendamentoId(Long) Optional~Atendimento~
    +findAllByPacienteIdOrderByRegistradoEmAsc(Long) List~Atendimento~
  }

  class Atendimento {
    +Long id
    +Long agendamentoId
    +Long pacienteId
    +Long medicoId
    +LocalDateTime registradoEm
    +String diagnostico
    +String observacoes
    +String prontuario
    +String receituario
    +String examesSolicitados
  }
```

## Relacionamento: Agendamento → Atendimento

- No código/entidades, **`atendimentos` possui `agendamento_id`** (campo `Atendimento.agendamentoId`).
- O service do atendimento impede duplicidade: ele valida se já existe atendimento para o `agendamentoId`.
- Assim, o relacionamento prático é:
  - **Agendamento (1)** → **Atendimento (0..1)**

```mermaid
classDiagram
  %% =========================
  %% Relacionamento Agendamento -> Atendimento
  %% =========================
  Agendamento "1" --> "0..1" Atendimento : agendamento_id
```

