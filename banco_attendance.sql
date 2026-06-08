CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

CREATE TABLE IF NOT EXISTS atendimentos (
    id                 BIGINT        AUTO_INCREMENT PRIMARY KEY,
    agendamento_id     BIGINT        NOT NULL UNIQUE,
    paciente_id        BIGINT        NOT NULL,
    medico_id          BIGINT        NOT NULL,
    registrado_em      DATETIME      NOT NULL,
    diagnostico        VARCHAR(1000) NULL,
    observacoes        VARCHAR(2000) NULL,
    prontuario         TEXT          NULL,
    receituario        TEXT          NULL,
    exames_solicitados TEXT          NULL,
    criado_em          DATETIME      NOT NULL,
    atualizado_em      DATETIME      NULL
);

INSERT INTO atendimentos (agendamento_id, paciente_id, medico_id, registrado_em, diagnostico, observacoes, prontuario, receituario, exames_solicitados, criado_em) VALUES
(6, 6, 5, '2026-05-15 14:30:00',
 'Dermatite atópica moderada',
 'Paciente apresenta lesões eritematosas em membros superiores. Relata piora nos últimos 15 dias.',
 'Paciente Lucas Oliveira, 30 anos. Histórico de alergia a níquel. Primeira consulta em 10/04/2026 com diagnóstico de dermatite leve.',
 'Hidrocortisona 1% creme - aplicar 2x ao dia por 14 dias. Loratadina 10mg - 1 comprimido à noite por 10 dias.',
 'Hemograma completo. IgE total sérica.',
 NOW()),
(7, 7, 6, '2026-05-10 11:30:00',
 'Consulta ginecológica de rotina',
 'Paciente sem queixas. Exames preventivos em dia.',
 'Fernanda Moraes, 43 anos. Histórico familiar de câncer de mama. Acompanhamento semestral.',
 'Sem prescrição necessária.',
 'Mamografia bilateral. Papanicolau. Ultrassonografia pélvica.',
 NOW());

