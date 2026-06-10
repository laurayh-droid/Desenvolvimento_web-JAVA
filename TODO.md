# TODO - Inicialização automática do banco no Docker

- [x] Atualizar `docker-compose.yml` para montar `banco_administrative.sql`, `banco_appointment.sql` e `banco_attendance.sql` em `/docker-entrypoint-initdb.d/` dos respectivos containers MySQL.

- [x] Validar comportamento “do zero” com `docker-compose down -v` e `docker-compose up --build`.

- [ ] Verificar no Workbench se as tabelas aparecem automaticamente.
- [ ] (Opcional) Confirmar que os módulos conectam sem erro após a criação do schema.

