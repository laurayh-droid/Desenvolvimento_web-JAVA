# Relação entre `Perfil` e `Usuario`

Este projeto utiliza duas estruturas complementares para representar o papel e as permissões de um usuário:

1. `Perfil`
2. `Usuario`

## Entidade `Perfil`

A entidade `Perfil` define o papel principal de um usuário no sistema. Cada perfil indica uma classificação básica que determina o tipo de usuário e o escopo geral de acesso:

- `MÉDICO`
- `FUNCIONÁRIO`
- `ADMINISTRADOR`

A tabela `perfis` armazena cada perfil com os seguintes campos:

- `id` (PK)
- `nome` (nome único do perfil)
- `descricao` (descrição opcional do perfil)

## Entidade `Usuario`

A entidade `Usuario` representa a conta que será usada para autenticação e autorização.

O `Usuario` mantém os seguintes dados principais:

- `idUser` — identificador único de login
- `senha` — senha criptografada ou armazenada de forma segura
- `funcionarioId` — referência ao funcionário associado
- `perfil` — relacionamento com a entidade `Perfil`
- `permissoes` — conjunto de permissões específicas atribuídas ao usuário
- `criadoEm` / `atualizadoEm` — timestamps de auditoria

## Relacionamento entre `Perfil` e `Usuario`

No modelo, cada `Usuario` possui um `Perfil` obrigatório:

- `Usuario` → `@ManyToOne Perfil`
- coluna de ligação: `perfil_id`

Isso significa que todo usuário já entra no sistema com uma classificação primária.

### Exemplo de uso do `Perfil`

- Um usuário com perfil `ADMINISTRADOR` pode ter acesso ao painel administrativo e operações de configuração.
- Um usuário com perfil `MÉDICO` pode acessar funcionalidades de consulta médica e prontuário.
- Um usuário com perfil `FUNCIONÁRIO` pode acessar funções operacionais ou de suporte.

## Permissões específicas por usuário

Além do perfil, cada usuário pode receber permissões específicas armazenadas em `permissoes`.

Esse conjunto de permissões é um `Set<String>` e é persistido em uma tabela associativa:

- tabela `usuario_permissoes`
- coluna `usuario_id` (FK para `Usuario`)
- coluna `permissao` (nome da permissão)

### Para que serve essa abordagem?

- `Perfil` define o papel geral do usuário.
- `permissoes` ajusta comportamentos específicos além do papel.

Por exemplo:

- Um `MÉDICO` pode receber permissões extras como `VER_RELATORIOS_FINANCEIROS` ou `EDITAR_PRONTUARIO_EXTERNO`.
- Um `FUNCIONÁRIO` pode receber permissão `ACESSO_AGENDAMENTOS` sem precisar de um perfil administrador.
- Um `ADMINISTRADOR` pode receber permissões específicas como `GERENCIAR_USUARIOS` ou `MODERAR_CONFIGURACOES`.

## Como o administrador concede permissões

Os endpoints de administração permitem criar e atualizar usuários com perfil e permissões.

### Criação de usuário

Ao cadastrar um usuário, o payload deve informar:

- `idUser`
- `senha`
- `funcionarioId`
- `perfilId`
- `permissoes`

Isso garante que o usuário já nasce com um perfil e com permissões específicas.

### Atualização de usuário

O administrador pode atualizar:

- `perfilId` — para mudar o papel principal do usuário
- `permissoes` — para ajustar permissões específicas de forma granular

## Comportamento esperado

- Perfis determinam regras de acesso globais e visibilidade de funcionalidades.
- Permissões específicas permitem refinamento ponto a ponto do que cada usuário pode fazer.
- Um usuário pode ter perfil `ADMINISTRADOR` e ainda receber permissões complementares ou restrições adicionais se o sistema assim tratar as regras.

## Vantagens desse design

- flexibilidade para separar papel principal e responsabilidades específicas
- capacidade de aplicar políticas de segurança mais precisas
- facilidade para o administrador configurar usuários sem alterar o perfil-base

## Observação

Caso a aplicação evolua para um modelo de autorização mais completo, é possível tratar `Perfil` como um conjunto de permissões padrão e combinar com `permissoes` individuais para cada conta.
