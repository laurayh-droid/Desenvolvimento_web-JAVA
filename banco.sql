CREATE DATABASE IF NOT EXISTS administrative_db;
USE administrative_db;

CREATE TABLE IF NOT EXISTS especialidades (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    descricao     VARCHAR(200) NOT NULL UNIQUE,
    criado_em     DATETIME     NOT NULL,
    atualizado_em DATETIME     NULL
);

CREATE TABLE IF NOT EXISTS medicos (
    id               BIGINT      AUTO_INCREMENT PRIMARY KEY,
    nome             VARCHAR(200) NOT NULL,
    crm              VARCHAR(50)  NOT NULL UNIQUE,
    especialidade_id BIGINT       NOT NULL,
    criado_em        DATETIME     NOT NULL,
    atualizado_em    DATETIME     NULL,
    CONSTRAINT fk_medicos_especialidade FOREIGN KEY (especialidade_id) REFERENCES especialidades(id)
);

CREATE TABLE IF NOT EXISTS convenios (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    nome_empresa  VARCHAR(200) NOT NULL,
    cnpj          VARCHAR(30)  NOT NULL UNIQUE,
    telefone      VARCHAR(30)  NULL,
    criado_em     DATETIME     NOT NULL,
    atualizado_em DATETIME     NULL
);

CREATE TABLE IF NOT EXISTS funcionarios (
    id               BIGINT       AUTO_INCREMENT PRIMARY KEY,
    nome_completo    VARCHAR(200) NOT NULL,
    rg               VARCHAR(30)  NOT NULL UNIQUE,
    cpf              VARCHAR(30)  NOT NULL UNIQUE,
    data_nascimento  DATETIME     NOT NULL,
    telefone_fixo    VARCHAR(30)  NULL,
    telefone_celular VARCHAR(30)  NULL,
    rua              VARCHAR(200) NULL,
    numero           VARCHAR(20)  NULL,
    complemento      VARCHAR(100) NULL,
    bairro           VARCHAR(100) NULL,
    cidade           VARCHAR(100) NULL,
    estado           VARCHAR(2)   NULL,
    cep              VARCHAR(20)  NULL,
    numero_ctps      VARCHAR(50)  NULL,
    numero_pis       VARCHAR(50)  NULL,
    criado_em        DATETIME     NOT NULL,
    atualizado_em    DATETIME     NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
    id             BIGINT       AUTO_INCREMENT PRIMARY KEY,
    id_user        VARCHAR(100) NOT NULL UNIQUE,
    senha          VARCHAR(200) NOT NULL,
    funcionario_id BIGINT       NOT NULL,
    criado_em      DATETIME     NOT NULL,
    atualizado_em  DATETIME     NULL,
    CONSTRAINT fk_usuarios_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id)
);

CREATE TABLE IF NOT EXISTS usuario_permissoes (
    usuario_id BIGINT       NOT NULL,
    permissao  VARCHAR(100) NOT NULL,
    CONSTRAINT fk_permissoes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

INSERT INTO especialidades (descricao, criado_em) VALUES
('Cardiologia',          NOW()),
('Pediatria',            NOW()),
('Clínica Geral',        NOW()),
('Ortopedia',            NOW()),
('Dermatologia',         NOW()),
('Ginecologia',          NOW()),
('Neurologia',           NOW()),
('Oftalmologia',         NOW());

INSERT INTO medicos (nome, crm, especialidade_id, criado_em) VALUES
('Dr. Roberto Andrade',     'CRM-MG-10234', 1, NOW()),
('Dra. Patrícia Fonseca',   'CRM-MG-20341', 2, NOW()),
('Dr. Marcos Vieira',       'CRM-MG-30452', 3, NOW()),
('Dra. Juliana Carvalho',   'CRM-MG-40563', 4, NOW()),
('Dr. Felipe Rodrigues',    'CRM-MG-50674', 5, NOW()),
('Dra. Beatriz Nascimento', 'CRM-MG-60785', 6, NOW());

INSERT INTO convenios (nome_empresa, cnpj, telefone, criado_em) VALUES
('Unimed Uberlândia',    '12.345.678/0001-90', '(34) 3218-1000', NOW()),
('Bradesco Saúde',       '51.990.695/0001-58', '(11) 3066-3400', NOW()),
('SulAmérica Saúde',     '01.685.903/0001-16', '(21) 3003-5551', NOW()),
('Hapvida',              '63.554.067/0001-98', '(85) 3101-0200', NOW()),
('Plano de Saúde Particular', '99.888.777/0001-11', '(34) 3333-9999', NOW());

INSERT INTO funcionarios (nome_completo, rg, cpf, data_nascimento, telefone_fixo, telefone_celular, rua, numero, bairro, cidade, estado, cep, numero_ctps, numero_pis, criado_em) VALUES
('Fernanda Costa Oliveira', 'MG-9876543', '404.555.666-77', '1992-03-15 00:00:00', '(34) 3241-1111', '(34) 99821-4444', 'Rua das Flores',     '120', 'Centro',        'Araguari',   'MG', '38440-010', '00123456789', '12345678901', NOW()),
('Ricardo Alves Pereira',   'MG-8765432', '505.666.777-88', '1988-07-20 00:00:00', '(34) 3241-2222', '(34) 99821-5555', 'Av. Brasil',         '450', 'Jardim América', 'Araguari',   'MG', '38440-020', '00234567890', '23456789012', NOW()),
('Camila Santos Reis',      'MG-7654321', '606.777.888-99', '1995-11-08 00:00:00', NULL,             '(34) 99821-6666', 'Rua Minas Gerais',   '88',  'Nova Araguari',  'Araguari',   'MG', '38440-030', '00345678901', '34567890123', NOW()),
('Diego Martins Souza',     'MG-6543210', '707.888.999-00', '1990-05-25 00:00:00', '(34) 3241-4444', '(34) 99821-7777', 'Rua Goiás',          '200', 'São Francisco',  'Uberlândia', 'MG', '38400-100', '00456789012', '45678901234', NOW());

INSERT INTO usuarios (id_user, senha, funcionario_id, criado_em) VALUES
('fernanda.costa',  'c3a1b2d4e5f6789012345678901234567890abcd', 1, NOW()),
('ricardo.alves',   'a1b2c3d4e5f6789012345678901234567890abcd', 2, NOW()),
('camila.santos',   'b2c3d4e5f6789012345678901234567890abcde', 3, NOW()),
('diego.martins',   'd4e5f6789012345678901234567890abcdef123', 4, NOW());

INSERT INTO usuario_permissoes (usuario_id, permissao) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_ADMIN'),
(4, 'ROLE_USER');


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
('Ana Paula Souza Lima',      'MG-1234567', '111.222.333-44', '1990-05-10 00:00:00', 'FEMALE', '(34) 99801-1111', TRUE,  'Unimed Uberlândia', 'Rua das Acácias',    '45',  'Centro',         'Araguari',   'MG', '38440-010', NOW()),
('Bruno Henrique Lima',       'MG-2345678', '222.333.444-55', '1985-08-22 00:00:00', 'MALE',   '(34) 99801-2222', FALSE, NULL,                'Av. Minas Gerais',   '890', 'Jardim América', 'Araguari',   'MG', '38440-020', NOW()),
('Carla Mendes Ferreira',     'MG-3456789', '333.444.555-66', '2000-01-15 00:00:00', 'FEMALE', '(34) 99801-3333', TRUE,  'Bradesco Saúde',    'Rua Bahia',          '12',  'Nova Araguari',  'Araguari',   'MG', '38440-030', NOW()),
('João Pedro Almeida',        'MG-4567890', '444.555.666-77', '1978-11-03 00:00:00', 'MALE',   '(34) 99801-4444', FALSE, NULL,                'Rua Goiás',          '300', 'São Francisco',  'Uberlândia', 'MG', '38400-100', NOW()),
('Maria Eduarda Rodrigues',   'MG-5678901', '555.666.777-88', '2005-03-28 00:00:00', 'FEMALE', '(34) 99801-5555', TRUE,  'SulAmérica Saúde',  'Rua das Palmeiras',  '67',  'Centro',         'Araguari',   'MG', '38440-010', NOW()),
('Lucas Oliveira Campos',     'MG-6789012', '666.777.888-99', '1995-07-14 00:00:00', 'MALE',   '(34) 99801-6666', FALSE, NULL,                'Av. Brasil',         '1200','Martins',        'Uberlândia', 'MG', '38400-200', NOW()),
('Fernanda Cristina Moraes',  'MG-7890123', '777.888.999-00', '1982-09-19 00:00:00', 'FEMALE', '(34) 99801-7777', TRUE,  'Hapvida',           'Rua Ceará',          '55',  'Tibery',         'Uberlândia', 'MG', '38400-300', NOW()),
('Rafael Souza Barbosa',      'MG-8901234', '888.999.000-11', '1970-12-01 00:00:00', 'MALE',   '(34) 99801-8888', FALSE, NULL,                'Rua Pará',           '78',  'Patrimônio',     'Uberlândia', 'MG', '38400-400', NOW());

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
