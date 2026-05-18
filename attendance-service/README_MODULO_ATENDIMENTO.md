# Módulo Atendimento - attendance-service

Este módulo registra o prontuário/histórico do paciente e permite a consulta dos atendimentos.

Endpoints:
- POST /api/v1/atendimentos
- GET  /api/v1/atendimentos/paciente/{pacienteId}
- GET  /api/v1/atendimentos/agendamento/{agendamentoId}

Modelo de dados:
- Tabela `atendimentos` (uma linha por atendimento, associada ao `agendamento_id`).

## SQL (MySQL) - Tabela `atendimentos`
```sql
CREATE TABLE atendimentos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    agendamento_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,

    registrado_em DATETIME NOT NULL,

    diagnostico VARCHAR(1000),
    observacoes VARCHAR(2000),
    prontuario VARCHAR(10000),
    receituario VARCHAR(10000),
    exames_solicitados VARCHAR(10000),

    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NULL
);

-- Observação:
-- - As regras de integridade (FKs/índices) podem ser ajustadas conforme o schema do projeto.
-- - O campo registrado_em é preenchido na aplicação caso não seja informado.
```


