# TODO - appointment-service (Agendamento)

## Passo 1 — Revisar exigências do MD vs código atual
- [x] Verificar endpoints e lógica do módulo de agendamento
- [x] Identificar lacunas: dados do paciente (endereço completo + telefone fixo/celular) e padronização/tradução

## Passo 2 — Implementar conformidade do cadastro do paciente
- [ ] Atualizar entidade `Paciente` para incluir endereço completo (rua, número, complemento, bairro, cidade, estado, CEP)
- [ ] Separar telefones: `telefoneFixo` e `telefoneCelular`
- [ ] Atualizar DTOs: `CriarPacienteRequest`, `AtualizarPacienteRequest`, `RespostaPaciente`
- [ ] Atualizar conversor `AgendamentoConversor` para mapear novos campos
- [ ] Garantir/atualizar validações (anotações Jakarta Validation)
- [ ] Atualizar repositório (se houver queries) e/ou schema JPA (colunas)

## Passo 3 — Traduzir/Padronizar o módulo para português
- [ ] Revisar variáveis/métodos/mensagens em:
  - `AgendamentoServiceImpl`
  - `AgendamentoConversor`
  - Controller/DTOs/exception messages
  - Swagger/OpenAPI annotations
- [ ] Manter compatibilidade do JSON (campos) conforme necessário

## Passo 4 — Testar
- [ ] Rodar `mvn test` no `appointment-service`
- [ ] Validar que o contexto sobe (smoke test) e que não há falhas de compilação

