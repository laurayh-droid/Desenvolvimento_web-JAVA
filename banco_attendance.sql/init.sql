CREATE TABLE IF NOT EXISTS atendimentos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    agendamento_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,

    registrado_em DATETIME NOT NULL,

    diagnostico VARCHAR(1000),
    observacoes VARCHAR(2000),
    prontuario LONGTEXT,
    receituario LONGTEXT,
    exames_solicitados LONGTEXT,

    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NULL
);

