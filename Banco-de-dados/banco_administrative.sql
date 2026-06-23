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
('Clinica Geral',        NOW()),
('Ortopedia',            NOW()),
('Dermatologia',         NOW()),
('Ginecologia',          NOW()),
('Neurologia',           NOW()),
('Oftalmologia',         NOW());

INSERT INTO medicos (nome, crm, especialidade_id, criado_em) VALUES
('Dr. Roberto Andrade',     'CRM-MG-10234', 1, NOW()),
('Dra. Patricia Fonseca',   'CRM-MG-20341', 2, NOW()),
('Dr. Marcos Vieira',       'CRM-MG-30452', 3, NOW()),
('Dra. Juliana Carvalho',   'CRM-MG-40563', 4, NOW()),
('Dr. Felipe Rodrigues',    'CRM-MG-50674', 5, NOW()),
('Dra. Beatriz Nascimento', 'CRM-MG-60785', 6, NOW());

INSERT INTO convenios (nome_empresa, cnpj, telefone, criado_em) VALUES
('Unimed Uberlândia',    '12.345.678/0001-90', '(34) 3218-1000', NOW()),
('Bradesco Saude',       '51.990.695/0001-58', '(11) 3066-3400', NOW()),
('SulAmérica Saude',     '01.685.903/0001-16', '(21) 3003-5551', NOW()),
('Hapvida',              '63.554.067/0001-98', '(85) 3101-0200', NOW()),
('Plano de Saude Particular', '99.888.777/0001-11', '(34) 3333-9999', NOW());

INSERT INTO funcionarios (nome_completo, rg, cpf, data_nascimento, telefone_fixo, telefone_celular, rua, numero, bairro, cidade, estado, cep, numero_ctps, numero_pis, criado_em) VALUES
('Fernanda Costa Oliveira', 'MG-9876543', '404.555.666-77', '1992-03-15 00:00:00', '(34) 3241-1111', '(34) 99821-4444', 'Rua das Flores',     '120', 'Centro',        'Araguari',   'MG', '38440-010', '00123456789', '12345678901', NOW()),
('Ricardo Alves Pereira',   'MG-8765432', '505.666.777-88', '1988-07-20 00:00:00', '(34) 3241-2222', '(34) 99821-5555', 'Av. Brasil',         '450', 'Jardim America', 'Araguari',   'MG', '38440-020', '00234567890', '23456789012', NOW()),
('Camila Santos Reis',      'MG-7654321', '606.777.888-99', '1995-11-08 00:00:00', NULL,             '(34) 99821-6666', 'Rua Minas Gerais',   '88',  'Nova Araguari',  'Araguari',   'MG', '38440-030', '00345678901', '34567890123', NOW()),
('Diego Martins Souza',     'MG-6543210', '707.888.999-00', '1990-05-25 00:00:00', '(34) 3241-4444', '(34) 99821-7777', 'Rua GoiÃ¡s',          '200', 'SÃ£o Francisco',  'UberlÃ¢ndia', 'MG', '38400-100', '00456789012', '45678901234', NOW());

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



