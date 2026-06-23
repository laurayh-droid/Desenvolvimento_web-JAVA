# Diagrama de Classes (Uso) — Sistema Completo IMEPAC Clínica Médica

## Módulo Commons (Compartilhado)

```mermaid
classDiagram
  %% =========================
  %% Commons - Entidades
  %% =========================
  class Usuario {
    +Long id
    +String nome
    +String email
    +String cpf
    +String rg
    +String telefone
    +String senha
    +Long perfilId
    +LocalDateTime criadoEm
  }

  class Paciente {
    +Long id
    +String nome
    +String cpf
    +String rg
    +String telefone
    +String email
    +String endereco
    +String cidade
    +String cep
    +Gender genero
    +LocalDate dataNascimento
  }

  class Medico {
    +Long id
    +String nome
    +String cpf
    +String crm
    +String email
    +String telefone
    +Long especialidadeId
    +Long convenioId
    +Boolean ativo
  }

  class Agendamento {
    +Long id
    +Long pacienteId
    +Long medicoId
    +LocalDateTime agendadoEm
    +StatusAgendamento status
    +String motivoCancelamento
    +String senhaCancelamentoHash
    +LocalDateTime agendamentoRetornoEm
    +String prontuario
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

  class Especialidade {
    +Long id
    +String nome
    +String descricao
    +String codigoAreaMedica
  }

  class Convenio {
    +Long id
    +String nome
    +String cnpj
    +String email
    +String telefone
    +String endereco
    +Boolean ativo
  }

  class Funcionario {
    +Long id
    +String nome
    +String cpf
    +String rg
    +String email
    +String telefone
    +String cargo
    +LocalDateTime dataAdmissao
  }

  class Perfil {
    +Long id
    +String nome
    +Set~String~ permissoes
  }

  class ApiResponse {
    +Boolean success
    +String message
    +T data
    +List~String~ errors
    +LocalDateTime timestamp
  }
```

---

## Módulo Administrativo (administrative-service)

```mermaid
classDiagram
  %% =========================
  %% Administrative Service
  %% =========================
  UsuarioController --> UsuarioService : usa
  MedicoController --> MedicoService : usa
  EspecialidadeController --> EspecialidadeService : usa
  ConvenioController --> ConvenioService : usa
  FuncionarioController --> FuncionarioService : usa

  UsuarioService <|-- UsuarioServiceImpl
  MedicoService <|-- MedicoServiceImpl
  EspecialidadeService <|-- EspecialidadeServiceImpl
  ConvenioService <|-- ConvenioServiceImpl
  FuncionarioService <|-- FuncionarioServiceImpl

  UsuarioServiceImpl --> UsuarioRepository : usa
  UsuarioServiceImpl --> PerfilRepository : usa
  UsuarioServiceImpl --> Usuario : cria/atualiza
  UsuarioServiceImpl --> Perfil : associa

  MedicoServiceImpl --> MedicoRepository : usa
  MedicoServiceImpl --> EspecialidadeRepository : usa
  MedicoServiceImpl --> ConvenioRepository : usa
  MedicoServiceImpl --> Medico : cria/atualiza

  EspecialidadeServiceImpl --> EspecialidadeRepository : usa
  EspecialidadeServiceImpl --> Especialidade : cria/atualiza

  ConvenioServiceImpl --> ConvenioRepository : usa
  ConvenioServiceImpl --> Convenio : cria/atualiza

  FuncionarioServiceImpl --> FuncionarioRepository : usa
  FuncionarioServiceImpl --> Funcionario : cria/atualiza

  class UsuarioController {
    +criarUsuario(CriarUsuarioRequest)
    +atualizarUsuario(usuarioId, AtualizarUsuarioRequest)
    +buscarUsuario(usuarioId)
    +listarUsuarios()
    +deletarUsuario(usuarioId)
  }

  class UsuarioService {
    +criarUsuario(CriarUsuarioRequest) RespostaUsuario
    +atualizarUsuario(Long, AtualizarUsuarioRequest) RespostaUsuario
    +buscarUsuario(Long) RespostaUsuario
    +listarUsuarios() List~RespostaUsuario~
    +deletarUsuario(Long) void
  }

  class UsuarioServiceImpl {
    -UsuarioRepository usuarioRepository
    -PerfilRepository perfilRepository
    -AdministracaoConversor conversor
  }

  class UsuarioRepository {
    +findByCpf(String)
    +findByEmail(String)
    +existsByCpf(String)
    +existsByEmail(String)
  }

  class MedicoController {
    +criarMedico(CriarMedicoRequest)
    +atualizarMedico(medicoId, AtualizarMedicoRequest)
    +buscarMedico(medicoId)
    +listarMedicos()
    +listarMedicosPorEspecialidade(especialidadeId)
  }

  class MedicoService {
    +criarMedico(CriarMedicoRequest) RespostaMedico
    +atualizarMedico(Long, AtualizarMedicoRequest) RespostaMedico
    +buscarMedico(Long) RespostaMedico
    +listarMedicos() List~RespostaMedico~
    +listarMedicosPorEspecialidade(Long) List~RespostaMedico~
  }

  class MedicoServiceImpl {
    -MedicoRepository medicoRepository
    -EspecialidadeRepository especialidadeRepository
    -ConvenioRepository convenioRepository
  }

  class MedicoRepository {
    +findByEspecialidadeId(Long)
    +findByCrm(String)
    +findByConvenioId(Long)
    +existsByCrm(String)
  }

  class EspecialidadeController {
    +criarEspecialidade(CriarEspecialidadeRequest)
    +atualizarEspecialidade(especialidadeId, AtualizarEspecialidadeRequest)
    +listarEspecialidades()
  }

  class EspecialidadeService {
    +criarEspecialidade(CriarEspecialidadeRequest) RespostaEspecialidade
    +atualizarEspecialidade(Long, AtualizarEspecialidadeRequest) RespostaEspecialidade
    +listarEspecialidades() List~RespostaEspecialidade~
  }

  class EspecialidadeServiceImpl {
    -EspecialidadeRepository especialidadeRepository
  }

  class EspecialidadeRepository {
    +findByNome(String)
  }

  class ConvenioController {
    +criarConvenio(CriarConvenioRequest)
    +atualizarConvenio(convenioId, AtualizarConvenioRequest)
    +listarConvenios()
  }

  class ConvenioService {
    +criarConvenio(CriarConvenioRequest) RespostaConvenio
    +atualizarConvenio(Long, AtualizarConvenioRequest) RespostaConvenio
    +listarConvenios() List~RespostaConvenio~
  }

  class ConvenioServiceImpl {
    -ConvenioRepository convenioRepository
  }

  class ConvenioRepository {
    +findByCnpj(String)
  }

  class FuncionarioController {
    +criarFuncionario(CriarFuncionarioRequest)
    +atualizarFuncionario(funcionarioId, AtualizarFuncionarioRequest)
    +listarFuncionarios()
  }

  class FuncionarioService {
    +criarFuncionario(CriarFuncionarioRequest) RespostaFuncionario
    +atualizarFuncionario(Long, AtualizarFuncionarioRequest) RespostaFuncionario
    +listarFuncionarios() List~RespostaFuncionario~
  }

  class FuncionarioServiceImpl {
    -FuncionarioRepository funcionarioRepository
  }

  class FuncionarioRepository {
    +findByCpf(String)
  }
```

---

## Módulo Agendamento (appointment-service)

```mermaid
classDiagram
  %% =========================
  %% Appointment Service
  %% =========================
  ConsultaController --> ConsultaService : usa
  PacienteController --> PacienteService : usa

  ConsultaService <|-- ConsultaServiceImpl
  PacienteService <|-- PacienteServiceImpl

  ConsultaServiceImpl --> AgendamentoRepository : usa
  ConsultaServiceImpl --> PacienteRepository : usa
  ConsultaServiceImpl --> Agendamento : cria/atualiza

  PacienteServiceImpl --> PacienteRepository : usa
  PacienteServiceImpl --> Paciente : cria/atualiza

  class ConsultaController {
    +agendarConsulta(CriarAgendamentoRequest)
    +cancelarConsulta(agendamentoId, CancelarAgendamentoRequest)
    +agendarRetorno(agendamentoId, AgendarRetornoRequest)
    +buscarAgendamentos(pacienteId)
  }

  class ConsultaService {
    +agendarConsulta(CriarAgendamentoRequest) RespostaAgendamento
    +cancelarConsulta(Long, CancelarAgendamentoRequest) RespostaAgendamento
    +agendarRetorno(Long, AgendarRetornoRequest) RespostaAgendamento
    +buscarAgendamentos(Long) List~RespostaAgendamento~
  }

  class ConsultaServiceImpl {
    -PacienteRepository pacienteRepository
    -AgendamentoRepository agendamentoRepository
    -AgendamentoConversor conversor
  }

  class AgendamentoRepository {
    +findAllByPacienteId(Long)
    +findByIdAndStatus(Long, StatusAgendamento)
    +existsByMedicoIdAndAgendadoEm(Long, LocalDateTime)
  }

  class PacienteController {
    +criarPaciente(CriarPacienteRequest)
    +atualizarPaciente(pacienteId, AtualizarPacienteRequest)
    +buscarPaciente(pacienteId)
    +buscarPacientePorCpf(cpf)
  }

  class PacienteService {
    +criarPaciente(CriarPacienteRequest) RespostaPaciente
    +atualizarPaciente(Long, AtualizarPacienteRequest) RespostaPaciente
    +buscarPaciente(Long) RespostaPaciente
    +buscarPacientePorCpf(String) RespostaPaciente
  }

  class PacienteServiceImpl {
    -PacienteRepository pacienteRepository
  }

  class PacienteRepository {
    +findByCpf(String)
    +existsByCpf(String)
    +findByRg(String)
  }
```

---

## Módulo Atendimento (attendance-service)

```mermaid
classDiagram
  %% =========================
  %% Attendance Service
  %% =========================
  RegistrarAtendimentoController --> RegistrarAtendimentoService : usa
  BuscarAtendimentoPorAgendamentoController --> BuscarAtendimentoPorAgendamentoService : usa
  ListarAtendimentosPorPacienteController --> ListarAtendimentosPorPacienteService : usa

  RegistrarAtendimentoService <|-- RegistrarAtendimentoServiceImpl
  BuscarAtendimentoPorAgendamentoService <|-- BuscarAtendimentoPorAgendamentoServiceImpl
  ListarAtendimentosPorPacienteService <|-- ListarAtendimentosPorPacienteServiceImpl

  RegistrarAtendimentoServiceImpl --> AtendimentoRepository : usa
  RegistrarAtendimentoServiceImpl --> Atendimento : cria/atualiza

  BuscarAtendimentoPorAgendamentoServiceImpl --> AtendimentoRepository : usa
  ListarAtendimentosPorPacienteServiceImpl --> AtendimentoRepository : usa

  class RegistrarAtendimentoController {
    +registrarAtendimento(CriarAtendimentoRequest)
    +atualizarAtendimento(atendimentoId, AtualizarAtendimentoRequest)
  }

  class RegistrarAtendimentoService {
    +registrarAtendimento(CriarAtendimentoRequest) RespostaAtendimento
    +atualizarAtendimento(Long, AtualizarAtendimentoRequest) RespostaAtendimento
  }

  class RegistrarAtendimentoServiceImpl {
    -AtendimentoRepository atendimentoRepository
    -AtendimentoConversor conversor
  }

  class BuscarAtendimentoPorAgendamentoController {
    +buscarPorAgendamento(agendamentoId)
  }

  class BuscarAtendimentoPorAgendamentoService {
    +buscarPorAgendamento(Long) RespostaAtendimento
  }

  class BuscarAtendimentoPorAgendamentoServiceImpl {
    -AtendimentoRepository atendimentoRepository
  }

  class ListarAtendimentosPorPacienteController {
    +listarPorPaciente(pacienteId)
  }

  class ListarAtendimentosPorPacienteService {
    +listarPorPaciente(Long) List~RespostaAtendimento~
  }

  class ListarAtendimentosPorPacienteServiceImpl {
    -AtendimentoRepository atendimentoRepository
  }

  class AtendimentoRepository {
    +findByAgendamentoId(Long) Optional~Atendimento~
    +findAllByPacienteIdOrderByRegistradoEmAsc(Long) List~Atendimento~
    +existsByAgendamentoId(Long) Boolean
  }
```

---

## Módulo Gateway (gateway-service)

```mermaid
classDiagram
  %% =========================
  %% Gateway Service
  %% =========================
  GatewayServiceApplication --> ConfiguracoesGateway : configura
  
  class GatewayServiceApplication {
    +main(String[] args) void
  }

  class ConfiguracoesGateway {
    -roteamento de requisições
    -balanceamento de carga
    -autenticação centralizada
    +rotearParaAdministrative()
    +rotearParaAppointment()
    +rotearParaAttendance()
  }

  note "API Gateway - Roteador centralizado de requisições entre serviços"
```

---

## Relacionamentos Entre Módulos

```mermaid
classDiagram
  %% =========================
  %% Relacionamentos Globais
  %% =========================
  Paciente "1" --> "*" Agendamento : agendador
  Medico "1" --> "*" Agendamento : realiza
  Especialidade "1" --> "*" Medico : tem
  Convenio "1" --> "*" Medico : conveniado

  Agendamento "1" --> "0..1" Atendimento : gera
  Paciente "1" --> "*" Atendimento : tem
  Medico "1" --> "*" Atendimento : realiza

  Usuario "1" --> "1" Perfil : possui

  note "Relacionamentos de negócio principais do sistema"
```

---

## Fluxo de Dados por Camada

```mermaid
graph TD
    A["Cliente HTTP<br/>(Postman/Frontend)"]
    B["Gateway Service<br/>(Roteador)"]
    C["Controller"]
    D["Service Interface"]
    E["Service Implementation"]
    F["Repository"]
    G["Entity/Database"]
    H["DTO Request/Response"]

    A -->|requisição| B
    B -->|roteia| C
    C -->|processa| D
    D -->|delega| E
    E -->|acessa| F
    F -->|executa SQL| G
    
    G -->|retorna| F
    F -->|cria Entity| E
    E -->|converte| H
    H -->|resposta JSON| C
    C -->|retorna| B
    B -->|responde| A

    style A fill:#e1f5ff
    style B fill:#fff3e0
    style C fill:#f3e5f5
    style D fill:#f3e5f5
    style E fill:#f3e5f5
    style F fill:#e8f5e9
    style G fill:#fce4ec
    style H fill:#fff9c4
```

---

## Padrões de Arquitetura Utilizados

### 1. **Camadas (Layered Architecture)**
- **Presentation Layer**: Controllers recebem requisições HTTP
- **Business Logic Layer**: Services implementam regras de negócio
- **Data Access Layer**: Repositories abstraem acesso ao banco de dados
- **Data Layer**: Entidades persistem no banco de dados

### 2. **Padrão DTO (Data Transfer Object)**
- Separação entre modelo de dados interno (Entity) e comunicação externa (DTO)
- Request DTOs para entrada de dados
- Response DTOs para saída de dados

### 3. **Padrão Repository**
- Abstração da camada de persistência
- Facilita testabilidade
- Encapsula queries JPA

### 4. **Padrão Service**
- Interface + Implementação
- Centraliza lógica de negócio
- Facilita testes com mocks

### 5. **Padrão Converter/Assembler**
- Conversão bidirecional entre DTO e Entity
- Isolamento de lógica de transformação

### 6. **API Gateway**
- Ponto único de entrada
- Roteamento centralizado
- Autenticação e autorização centralizadas

### 7. **Microsserviços**
- Serviços independentes (administrative, appointment, attendance)
- Banco de dados por serviço
- Comunicação via HTTP/REST

---

## Tecnologias Utilizadas

- **Framework**: Spring Boot 3.x
- **Linguagem**: Java 17+
- **ORM**: JPA/Hibernate
- **Banco de Dados**: MySQL
- **API REST**: Spring Web
- **Comunicação Inter-serviços**: OpenFeign
- **Containerização**: Docker
- **Orquestração**: Kubernetes
- **Dependency Injection**: Spring IoC Container
