# Guia de Comandos do Projeto

Este documento lista os principais comandos utilizados no terminal e no banco de dados para subir o ambiente de microsserviços e verificar os dados.

## Comandos Docker

### `docker-compose up -d`
- **Para que serve:** Inicia todos os serviços (bancos de dados e aplicações Java) definidos no arquivo `docker-compose.yml`. A flag `-d` (detached) faz com que os containers rodem em segundo plano, liberando o seu terminal para continuar sendo usado.
- **Quando usar:** Sempre que quiser ligar o seu projeto.

### `docker-compose down -v`
- **Para que serve:** Para e remove todos os containers, redes e **volumes** criados pelo `docker-compose up`.
- **Quando usar:** Útil para "resetar" o ambiente do zero. A flag `-v` (volumes) é muito importante, pois ela apaga os discos virtuais de dados do banco. Se o seu banco de dados estiver com erro de permissão ou "bugado", usar esse comando fará com que o MySQL seja recriado do zero na próxima vez que você subir os containers.

### `docker ps`
- **Para que serve:** Lista os containers que estão rodando no momento. 
- **Comando extra usado:** `docker ps --filter "name=mysql-" --format "table {{.Names}}\t{{.Status}}"`
- **Explicação:** Esse comando específico que usamos filtrou apenas os containers que tinham a palavra "mysql" no nome e mostrou uma tabela limpa apenas com os nomes e o status deles. Foi ótimo para verificar se eles já estavam como `healthy` (saudáveis e prontos para conexão).

---

## Comandos MySQL / SQL

### Execução de Scripts `.sql`
- **Para que serve:** Os arquivos `banco_appointment.sql`, `banco_administrative.sql` e `banco_attendance.sql` são scripts que contêm instruções do tipo `CREATE TABLE` (criar tabelas) e `INSERT INTO` (inserir dados).
- **Como usamos:** Abrimos cada arquivo na sua respectiva conexão/porta no MySQL Workbench e clicamos no ícone do raio ⚡. Isso foi necessário porque os bancos MySQL iniciam em branco.

### `SELECT * FROM nome_da_tabela;`
- **Para que serve:** Busca e exibe todas (`*`) as colunas e todos os registros de uma tabela específica.
- **Quando usar:** Para verificar se os dados foram inseridos corretamente ou para ver as informações que o seu sistema Java acabou de salvar no banco.
- **Exemplos que usamos:**
  - `SELECT * FROM atendimentos;` *(Executado na porta 3311, banco attendance_db)*
  - `SELECT * FROM pacientes;` *(Executado na porta 3309, banco appointment_db)*
  - `SELECT * FROM agendamentos;` *(Executado na porta 3309, banco appointment_db)*
  - `SELECT * FROM medicos;` *(Executado na porta 3310, banco administrative_db)*

---
*Dica para o MySQL Workbench: lembre-se sempre de clicar com o botão direito e escolher **"Refresh All"** no menu lateral esquerdo para ver as tabelas recém-criadas.*
