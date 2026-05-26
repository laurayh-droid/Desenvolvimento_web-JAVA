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
 'Dermatite atÃ³pica moderada',
 'Paciente apresenta lesÃµes eritematosas em membros superiores. Relata piora nos Ãºltimos 15 dias.',
 'Paciente Lucas Oliveira, 30 anos. HistÃ³rico de alergia a nÃ­quel. Primeira consulta em 10/04/2026 com diagnÃ³stico de dermatite leve.',
 'Hidrocortisona 1% creme - aplicar 2x ao dia por 14 dias. Loratadina 10mg - 1 comprimido Ã  noite por 10 dias.',
 'Hemograma completo. IgE total sÃ©rica.',
 NOW()),
(7, 7, 6, '2026-05-10 11:30:00',
 'Consulta ginecolÃ³gica de rotina',
 'Paciente sem queixas. Exames preventivos em dia.',
 'Fernanda Moraes, 43 anos. HistÃ³rico familiar de cÃ¢ncer de mama. Acompanhamento semestral.',
 'Sem prescriÃ§Ã£o necessÃ¡ria.',
 'Mamografia bilateral. Papanicolau. Ultrassonografia pÃ©lvica.',
 NOW());

