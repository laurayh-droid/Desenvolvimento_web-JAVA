# 17.1 Descrição

Nesta parte derradeira do livro, vamos desenvolver um projeto completo de aplicação web utilizando os re-cursos já estudados até agora e outros que ainda veremos, como acesso a banco de dados relacional. Neste projeto, faremos uso da arquitetura MVC, com páginas JSP e Servlets. Além disso, como mencionamos, haverá também funcionalidade de acesso a um banco de dados relacional.

O projeto que desenvolveremos é o de uma aplicação para gerenciamento de uma clínica médica. Essa aplica-ção será composta por três módulos, todos eles destinados a uso interno por parte da clínica.

Os conhecimentos adquiridos no processo de desenvolvimento desse projeto podem ser perfeitamente utiliza-dos na construção de outros tipos de sites.

A partir dessa aplicação, o atendente poderá agendar consultas de pacientes e marcar retorno. O paciente pode agendar uma consulta para atendimento particular ou por convênios aceitos pela clínica. O agendamento de consulta somente é possível se o paciente já estiver cadastrado. No caso de não haver cadastro, este deve ser realizado pelo atendente.

Os médicos que atendem na clínica, por outro lado, poderão registrar e acompanhar pelo próprio sistema todo o prontuário/histórico do paciente.


## Para acessar o sistema, tanto o(s) atendente(s) como o(s) médico(s) deverão efetuar o login, que habilitará as opções corretas para o perfil do usuário.

As páginas/telas serão desenvolvidas com o uso de folhas de estilo (CSS) e imagens gráficas. Essas imagens es-tão incluídas no arquivo que pode ser baixado do site da Editora Érica.

Nas Tabelas 17.1 a 17.3 estão listados os Módulos Administrativo, Agendamento e Atendimento, com suas res-pectivas funcionalidades.

### Tabela 17.1-Módulo Administrativo

*Funcionalidade*                           *Descrição* 
                                              
**Cadastro de funcionários**:              Permite o cadastro de funcionários da clínica..

**Cadastro de usuários**:                  Permite o cadastro de usuários do sistema e suas permissões de acesso.

**Cadastro de especialidades**:            Permite cadastrar as especialidades médicas que podem ser atendidas pela clínica.

**Cadastro de médicos**:                   Permite cadastrar os médicos que atendem pela clínica, com suas respectivas especialidades.

**Cadastro de convênios**:                 Permite cadastrar os convênios aceitos pela clínica.


### Tabela 17.2 - Módulo Agendamento

*Funcionalidade*                           *Descrição* 

**Cadastro de pacientes**:                 Permite cadastrar os pacientes atendidos pela clínica.

**Agenda de consulta**:                    Permite registrar uma consulta para o paciente (dia e hora).

**Registro de retorno**:                   Permite registrar uma data de retorno ao médico.

**Cancelamento de consulta**:              Permite cancelar uma consulta anteriormente agendada pelo paciente.

### Tabela 17.3 Módulo Atendimento

*Funcionalidade*                       *Descrição*

**Prontuário do paciente**:             Permite ao médico visualizar o histórico/prontuário do paciente.

**Registro de atendimento**:            Permite ao médico registrar informações relativas à consulta, como resultados de exames ou diagnóstico.

**Receituário**:                        Permite que o médico registre os medicamentos receitados ao paciente.

**Exames**:                             Permite o registro de exames solicitados 
pelo médico.

** O módulo Atendimento deve gravar os dados: (1) em uma única tabela por atendimento (aggregate)



### 17.2 Módulo Administrativo

Por ser o módulo que permite a administração do banco de dados do aplicativo, este será o primeiro a detalhar-mos e desenvolver.

Conforme visto anteriormente, este módulo será utilizado para que possamos cadastrar funcionários, usuários, especialidades, médicos e convênios. Todos esses cadastros devem permitir a inclusão de novos registros, ex-clusão de registros existentes, alteração de dados de registros e consülta/visualização.

Para o cadastro de funcionários serão necessários os seguintes dados: nome completo, número do RG, número do CPF, endereço completo (rua, número, complemento, bairro, cidade, estado e CEP), telefones (fixo e celular), número da CTPS, número do PIS.

O cadastro de usuário é efetuado a partir do cadastro de funcionários. Sendo assim, é necessário que o funcionário já esteja devidamente cadastrado. A tela deve permitir a seleção do funcionário por meio de uma caixa de combinação. Os dados de identificação do usuário (iduser) e senha de acesso (password) serão informados, assim como as permissões de acesso ao sistema. O cadastro de especialidades é bastante simples, e a única informação a ser digitada é a descrição.

A seguir temos o cadastro de médicos, que deve conter nome do médico, número do CRM e especialidade.Por fim, o cadastro de convênios, que terá apenas o nome da empresa, CNPJ e telefone.


### 17.3 Módulo Agendamento

Este talvez seja o módulo mais complexo, pois permite a execução de várias tarefas. Em primeiro lugar, temos o cadastro de pacientes, que deve conter, como dados, o nome completo, número do RG, número do CPF, endereço completo (rua, número, complemento, cidade, estado e CEP), telefones (fixo e celular), data de nascimen-to, sexo, se possui convênio/plano de saúde e de qual empresa.

A agenda de consulta deve permitir ao atendente pesquisar o día e o horário que estão disponíveis para o paciente. Quando houver necessidade de retorno do paciente, procedimento similar deverá ser executado.


A opção de cancelamento de consulta, quando solicitado pelo paciente, deve liberar no calendário de consultas o dia e hora alocados anteriormente. Para efetivação do cancelamento, o sistema deve solicitar o motivo e uma senha.


### 17.4 Módulo Atendimento

Neste módulo, acessível apenas pelos médicos, serão registrados os dados relativos à consulta, como informa-ções passadas pelo paciente, diagnóstico médico ou solicitação de exames.

Todos os dados são gravados em um histórico do prontuário do paciente, que pode ser consultado pelo médi-co. O sistema também deve registrar o receituário e os exames solicitados.

### 17.5 Definição de dados para o sistema

As tabelas a seguir relacionam os principais dados que devem ser armazenados em cada uma das funcionalida-des dos módulos.

Tabela 17.4-Módulo Administrativo

*Funcionalidade*                          
Cadastro de funcionários: **Informação** --> Nome do funcionário                     

Número do RG

Órgão emissor

Número do CPF

Endereço

Número

Complemento

Bairro

Cidade

Estado

Telefone

Celular

Número da CTPS

Número do PIS

Data de nascimento

https://chatgpt.com/share/69ea9bc7-b8c0-83e9-9518-2dd09260adb5
                                    