CREATE DATABASE IF NOT EXISTS appointment_db;
USE appointment_db;

CREATE TABLE IF NOT EXISTS pacientes (
    id                  BIGINT       AUTO_INCREMENT PRIMARY KEY,
    nome_completo       VARCHAR(200) NOT NULL,
    rg                  VARCHAR(30)  NOT NULL UNIQUE,
    cpf                 VARCHAR(30)  NOT NULL UNIQUE,
    data_nascimento     DATETIME     NOT NULL,
    genero              VARCHAR(10)  NOT NULL,
    telefone            VARCHAR(30)  NULL,
    telefone_fixo       VARCHAR(30)  NULL,
    telefone_celular    VARCHAR(30)  NULL,
    has_seguro          BOOLEAN      NOT NULL DEFAULT FALSE,
    rua                 VARCHAR(200) NULL,
    numero              VARCHAR(20)  NULL,
    complemento         VARCHAR(100) NULL,
    bairro              VARCHAR(100) NULL,
    cidade              VARCHAR(100) NULL,
    estado              VARCHAR(2)   NULL,
    cep                 VARCHAR(20)  NULL,
    nome_empresa_seguro VARCHAR(200) NULL,
    criado_em           DATETIME     NOT NULL,
    atualizado_em       DATETIME     NULL
);

CREATE TABLE IF NOT EXISTS agendamentos (
    id                      BIGINT       AUTO_INCREMENT PRIMARY KEY,
    paciente_id             BIGINT       NOT NULL,
    medico_id               BIGINT       NOT NULL,
    agendado_em             DATETIME     NOT NULL,
    status                  VARCHAR(30)  NOT NULL DEFAULT 'AGENDADO',
    motivo_cancelamento     VARCHAR(500) NULL,
    senha_cancelamento_hash VARCHAR(255) NULL,
    agendamento_retorno_em  DATETIME     NULL,
    prontuario              TEXT         NULL,
    criado_em               DATETIME     NOT NULL,
    atualizado_em           DATETIME     NULL,
    CONSTRAINT fk_agendamentos_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

INSERT INTO pacientes (nome_completo, rg, cpf, data_nascimento, genero, telefone_celular, has_seguro, nome_empresa_seguro, rua, numero, bairro, cidade, estado, cep, criado_em) VALUES
('Ana Paula Souza Lima',      'MG-1234567', '111.222.333-44', '1990-05-10 00:00:00', 'FEMALE', '(34) 99801-1111', TRUE,  'Unimed UberlÃ¢ndia', 'Rua das AcÃ¡cias',    '45',  'Centro',         'Araguari',   'MG', '38440-010', NOW()),
('Bruno Henrique Lima',       'MG-2345678', '222.333.444-55', '1985-08-22 00:00:00', 'MALE',   '(34) 99801-2222', FALSE, NULL,                'Av. Minas Gerais',   '890', 'Jardim AmÃ©rica', 'Araguari',   'MG', '38440-020', NOW()),
('Carla Mendes Ferreira',     'MG-3456789', '333.444.555-66', '2000-01-15 00:00:00', 'FEMALE', '(34) 99801-3333', TRUE,  'Bradesco SaÃºde',    'Rua Bahia',          '12',  'Nova Araguari',  'Araguari',   'MG', '38440-030', NOW()),
('João Pedro Almeida',        'MG-4567890', '444.555.666-77', '1978-11-03 00:00:00', 'MALE',   '(34) 99801-4444', FALSE, NULL,                'Rua GoiÃ¡s',          '300', 'SÃ£o Francisco',  'UberlÃ¢ndia', 'MG', '38400-100', NOW()),
('Maria Eduarda Rodrigues',   'MG-5678901', '555.666.777-88', '2005-03-28 00:00:00', 'FEMALE', '(34) 99801-5555', TRUE,  'SulAmÃ©rica SaÃºde',  'Rua das Palmeiras',  '67',  'Centro',         'Araguari',   'MG', '38440-010', NOW()),
('Lucas Oliveira Campos',     'MG-6789012', '666.777.888-99', '1995-07-14 00:00:00', 'MALE',   '(34) 99801-6666', FALSE, NULL,                'Av. Brasil',         '1200','Martins',        'UberlÃ¢ndia', 'MG', '38400-200', NOW()),
('Fernanda Cristina Moraes',  'MG-7890123', '777.888.999-00', '1982-09-19 00:00:00', 'FEMALE', '(34) 99801-7777', TRUE,  'Hapvida',           'Rua CearÃ¡',          '55',  'Tibery',         'UberlÃ¢ndia', 'MG', '38400-300', NOW()),
('Rafael Souza Barbosa',      'MG-8901234', '888.999.000-11', '1970-12-01 00:00:00', 'MALE',   '(34) 99801-8888', FALSE, NULL,                'Rua ParÃ¡',           '78',  'PatrimÃ´nio',     'UberlÃ¢ndia', 'MG', '38400-400', NOW());

INSERT INTO agendamentos (paciente_id, medico_id, agendado_em, status, criado_em) VALUES
(1, 1, '2026-05-20 08:00:00', 'AGENDADO',         NOW()),
(2, 3, '2026-05-20 09:00:00', 'AGENDADO',         NOW()),
(3, 2, '2026-05-20 10:00:00', 'AGENDADO',         NOW()),
(4, 4, '2026-05-21 08:30:00', 'AGENDADO',         NOW()),
(5, 1, '2026-05-21 09:30:00', 'AGENDADO',         NOW()),
(6, 5, '2026-05-15 14:00:00', 'RETORNO_AGENDADO', NOW()),
(7, 6, '2026-05-10 11:00:00', 'CANCELADO',        NOW()),
(8, 3, '2026-05-08 08:00:00', 'CANCELADO',        NOW());

UPDATE agendamentos SET motivo_cancelamento = 'Paciente desmarcou por motivo pessoal', senha_cancelamento_hash = '-1985234512' WHERE id = 7;
UPDATE agendamentos SET motivo_cancelamento = 'Médico indisponível na data solicitada', senha_cancelamento_hash = '987654321'  WHERE id = 8;



