# Guia Completo de Tecnologias — Sistema de Pedidos (Order System)

> **Público-alvo:** Alunos do curso de Análise e Desenvolvimento de Sistemas (ADS) aprendendo desenvolvimento de APIs com Java e Spring Boot.
>
> **Como usar este guia:** Cada seção explica **o que é** a tecnologia, **por que ela existe neste projeto**, **como ela funciona na prática** (com exemplos do próprio código) e **quais alternativas existem** no mercado. Ao final da leitura, você deve ser capaz de explicar cada escolha tecnológica e tomar decisões similares em projetos futuros.

---

## Sumário

1. [Visão Geral da Arquitetura](#1-visão-geral-da-arquitetura)
2. [Maven — Gerenciamento de Build e Dependências](#2-maven--gerenciamento-de-build-e-dependências)
3. [Spring Boot — O Framework Principal](#3-spring-boot--o-framework-principal)
4. [Spring MVC — Construindo a API REST](#4-spring-mvc--construindo-a-api-rest)
5. [Spring Data JPA — Persistência de Dados](#5-spring-data-jpa--persistência-de-dados)
6. [MySQL — Banco de Dados Relacional](#6-mysql--banco-de-dados-relacional)
7. [Lombok — Eliminando Código Repetitivo](#7-lombok--eliminando-código-repetitivo)
8. [Bean Validation — Validação de Dados de Entrada](#8-bean-validation--validação-de-dados-de-entrada)
9. [OpenFeign — Comunicação Entre Microsserviços](#9-openfeign--comunicação-entre-microsserviços)
10. [OpenAPI / Swagger — Documentação da API](#10-openapi--swagger--documentação-da-api)
11. [Logbook — Logging de Requisições HTTP](#11-logbook--logging-de-requisições-http)
12. [Padrões de Projeto e Arquitetura em Camadas](#12-padrões-de-projeto-e-arquitetura-em-camadas)
13. [Tratamento Global de Exceções](#13-tratamento-global-de-exceções)
14. [Configuração com application.yml](#14-configuração-com-applicationyml)
15. [Spring Auto-configuration — Módulo Commons Compartilhado](#15-spring-auto-configuration--módulo-commons-compartilhado)
16. [Docker — Containerização](#16-docker--containerização)
17. [Docker Compose — Orquestração de Containers](#17-docker-compose--orquestração-de-containers)
18. [Testes Automatizados](#18-testes-automatizados)
19. [Tipos Java Fundamentais Usados no Projeto](#19-tipos-java-fundamentais-usados-no-projeto)
20. [Jackson — Serialização e Desserialização JSON](#20-jackson--serialização-e-desserialização-json)
21. [Postman — Teste Manual de APIs](#21-postman--teste-manual-de-apis)
22. [Script de Build (build-and-run.sh)](#22-script-de-build-build-and-runsh)

---

## 1. Visão Geral da Arquitetura

### O que é Arquitetura de Microsserviços?

Este projeto implementa uma **arquitetura de microsserviços**: em vez de um único sistema monolítico contendo toda a lógica (cadastro de clientes, pedidos, pagamentos etc.), o sistema é dividido em serviços independentes, cada um com sua própria responsabilidade, banco de dados e ciclo de implantação.

### Como está estruturado este projeto

```
order-system/                  ← projeto pai (parent POM)
├── commons/                   ← módulo compartilhado (DTOs, exceções, handler)
├── customer-service/          ← microsserviço de clientes (porta 8081)
└── order-service/             ← microsserviço de pedidos (porta 8082)
```

**customer-service** gerencia o ciclo de vida dos clientes (criar, buscar, atualizar, desativar). Possui seu próprio banco de dados MySQL (`customer_db`).

**order-service** gerencia pedidos. Antes de criar um pedido, ele consulta o `customer-service` para verificar se o cliente existe e está ativo. Possui seu próprio banco de dados MySQL (`order_db`).

**commons** é um módulo Java puro que contém classes compartilhadas entre os dois serviços: DTOs de contrato entre os serviços, exceções customizadas e o handler global de erros. Ele não é um serviço — é uma biblioteca interna.

### Por que microsserviços?

| Monolítico | Microsserviços |
|---|---|
| Tudo em um único processo | Cada serviço é um processo independente |
| Escala o sistema inteiro | Escala apenas o serviço com gargalo |
| Deploy único e arriscado | Deploy independente por serviço |
| Uma linguagem/tecnologia | Cada serviço pode usar tecnologias diferentes |
| Mais simples de começar | Mais complexo de operar, mas mais resiliente |

### Alternativas à arquitetura de microsserviços

- **Monolito modular:** Um único deploy, mas com pacotes Java bem separados. Ideal para times pequenos e projetos novos. Netflix e Shopify começaram assim.
- **Monolito modular + extração gradual:** Começa monolítico, depois extrai microsserviços conforme a necessidade. Estratégia recomendada por Martin Fowler ("MonolithFirst").
- **Serverless / Functions-as-a-Service:** Cada endpoint é uma função independente (AWS Lambda, Google Cloud Functions). Elimina a gestão de servidores, mas dificulta o estado compartilhado.

---

## 2. Maven — Gerenciamento de Build e Dependências

### O que é o Maven?

Maven é a ferramenta de **build e gerenciamento de dependências** mais popular no ecossistema Java. Ele define o ciclo de vida do projeto (compilar, testar, empacotar, instalar) e busca automaticamente as bibliotecas necessárias no Maven Central Repository.

A configuração do Maven fica em arquivos chamados `pom.xml` (Project Object Model).

### A estrutura multi-módulo deste projeto

No arquivo `pom.xml` raiz:

```xml
<groupId>com.imepac</groupId>
<artifactId>order-system</artifactId>
<version>1.0.0</version>
<packaging>pom</packaging>   <!-- "pom" = projeto pai, não gera um JAR -->

<modules>
    <module>commons</module>
    <module>customer-service</module>
    <module>order-service</module>
</modules>
```

O `packaging` igual a `pom` indica que este projeto é um **aggregator**: ele não gera um artefato executável, mas define configurações herdadas pelos módulos filhos.

### Por que usar multi-módulo?

Permite que os módulos `customer-service` e `order-service` herdem configurações comuns do pai (versão do Java, versão do Spring Boot) sem duplicação. Se precisar mudar a versão do Spring Boot, muda apenas no POM pai.

Os módulos filhos declaram o pai assim:

```xml
<parent>
    <groupId>com.imepac</groupId>
    <artifactId>order-system</artifactId>
    <version>1.0.0</version>
</parent>
```

### dependencyManagement — Controle Centralizado de Versões

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

A seção `<dependencyManagement>` define versões sem incluir as dependências. Os módulos filhos podem declarar `spring-boot-starter-web` sem informar a versão — ela vem do pai. Isso evita conflitos de versão entre módulos.

O `<scope>import</scope>` com `<type>pom</type>` é um padrão chamado **Bill of Materials (BOM)**: importa um catálogo inteiro de versões compatíveis entre si.

### properties — Variáveis Reutilizáveis

```xml
<properties>
    <java.version>17</java.version>
    <spring-boot.version>3.2.0</spring-boot.version>
    <spring-cloud.version>2023.0.0</spring-cloud.version>
    <logbook.version>3.8.0</logbook.version>
    <springdoc.version>2.3.0</springdoc.version>
</properties>
```

Variáveis referencidas com `${nome.variavel}`. Mudar a versão do Spring Boot em um único lugar atualiza todos os módulos.

### Scopes de dependências

No `customer-service/pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>   <!-- só necessário em tempo de execução -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>      <!-- só disponível nos testes -->
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional> <!-- não é transitiva: quem depende deste módulo não herda o Lombok -->
</dependency>
```

| Scope | Quando disponível |
|---|---|
| `compile` (padrão) | Compilação, testes e execução |
| `runtime` | Apenas em execução (não é necessário para compilar) |
| `test` | Apenas durante testes |
| `provided` | Disponível em compilação, mas fornecido pelo ambiente (ex: servidor de aplicação) |

### O plugin spring-boot-maven-plugin

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </exclude>
        </excludes>
    </configuration>
</plugin>
```

Este plugin gera um **fat JAR** (JAR executável com todas as dependências embutidas) que pode ser executado com `java -jar app.jar`. O Lombok é excluído porque ele é um processador de anotações que age apenas em tempo de compilação — não é necessário em runtime.

### Comandos Maven essenciais

```bash
mvn clean install          # limpa, compila, testa e instala no repositório local
mvn clean package          # compila e empacota sem instalar
mvn clean package -DskipTests  # empacota ignorando os testes
mvn test                   # executa apenas os testes
```

### Alternativas ao Maven

- **Gradle:** Usa uma DSL em Groovy ou Kotlin em vez de XML. Mais rápido (cache inteligente e build incremental), preferido em projetos Android e cada vez mais comum em projetos Spring. A maioria dos tutoriais modernos do Spring Initializr usa Gradle.
- **Ant:** Mais antigo e mais verboso. Exige que você descreva cada etapa manualmente. Raramente usado em projetos novos.
- **Bazel / Buck:** Ferramentas de build da Google/Meta para monorepos gigantes com múltiplas linguagens.

---

## 3. Spring Boot — O Framework Principal

### O que é o Spring Boot?

O Spring Framework existe desde 2002 e revolucionou o desenvolvimento Java empresarial com injeção de dependência e programação orientada a aspectos. Porém, configurá-lo exigia muitos arquivos XML e muito código boilerplate.

O **Spring Boot** (lançado em 2014) é uma camada sobre o Spring Framework que aplica o princípio de **"convention over configuration"** (convenção sobre configuração): ele faz escolhas razoáveis por padrão, e você só precisa configurar o que difere do padrão.

### @SpringBootApplication

```java
@SpringBootApplication(scanBasePackages = "com.imepac")
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
```

`@SpringBootApplication` é uma meta-anotação que combina três anotações:

- **`@SpringBootConfiguration`:** Indica que esta classe pode definir Beans (como `@Configuration`).
- **`@EnableAutoConfiguration`:** Ativa a auto-configuração do Spring Boot. Ele analisa o classpath e configura automaticamente o que encontrar (se há `spring-boot-starter-web`, configura um servidor Tomcat; se há `spring-boot-starter-data-jpa`, configura o EntityManager etc.).
- **`@ComponentScan`:** Varre o pacote base e subpacotes em busca de anotações como `@Controller`, `@Service`, `@Repository`, `@Component` para registrar os Beans no contexto Spring.

O parâmetro `scanBasePackages = "com.imepac"` é necessário porque as classes do módulo `commons` estão em um pacote diferente do pacote principal do serviço. Sem isso, o Spring não encontraria o `GlobalExceptionHandler` do commons.

### Injeção de Dependência — o coração do Spring

O Spring mantém um **container de objetos** chamado **Application Context**. Quando a aplicação sobe, o Spring instancia todos os Beans (objetos gerenciados) e injeta as dependências automaticamente.

No projeto, isso aparece de várias formas:

```java
// Forma 1: @RequiredArgsConstructor do Lombok gera o construtor
// O Spring detecta um único construtor e injeta automaticamente
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository; // injetado pelo Spring
}
```

```java
// Forma 2: @Autowired (mais verboso, não recomendado para campos finais)
@Autowired
private CustomerRepository customerRepository;
```

A injeção por construtor (usada neste projeto) é preferida porque:
1. Garante que o objeto é sempre criado com suas dependências completas.
2. Facilita os testes (você pode criar a instância manualmente passando mocks).
3. Permite usar `final`, garantindo imutabilidade.

### Por que o Spring Boot?

Sem o Spring Boot, você precisaria:
- Configurar manualmente o servidor web (Tomcat, Jetty).
- Definir todos os Beans em XML ou classes de configuração.
- Gerenciar versões compatíveis de dezenas de dependências.
- Escrever código para ler arquivos de configuração.

Com o Spring Boot, você adiciona as dependências certas e a aplicação já funciona.

### Alternativas ao Spring Boot

- **Quarkus:** Framework Java focado em tempo de inicialização ultra-rápido e baixo consumo de memória, ideal para ambientes de nuvem e serverless. Suporta compilação para executável nativo com GraalVM.
- **Micronaut:** Similar ao Quarkus, processa anotações em tempo de compilação (não em runtime), o que o torna mais rápido para inicializar.
- **Helidon (Oracle):** Focado em microsserviços, suporta as especificações Jakarta EE/MicroProfile.
- **Jakarta EE puro (com WildFly/Payara):** O padrão empresarial Java sem o Spring. Mais verboso mas totalmente padronizado.
- **Vert.x:** Framework reativo para alta concorrência com modelo de event loop (similar ao Node.js).

---

## 4. Spring MVC — Construindo a API REST

### O que é REST?

**REST** (Representational State Transfer) é um estilo arquitetural para sistemas distribuídos. Uma API RESTful expõe **recursos** (clientes, pedidos) acessíveis por **URLs** e manipuláveis por **métodos HTTP** (verbos), retornando representações dos recursos geralmente em **JSON**.

### Verbos HTTP e seus significados

| Verbo HTTP | Anotação Spring MVC | Semântica | Resposta típica |
|---|---|---|---|
| `GET` | `@GetMapping` | Recuperar um recurso | 200 OK |
| `POST` | `@PostMapping` | Criar um novo recurso | 201 Created |
| `PUT` | `@PutMapping` | Substituir completamente um recurso | 200 OK |
| `PATCH` | `@PatchMapping` | Atualizar parcialmente um recurso | 200 OK |
| `DELETE` | `@DeleteMapping` | Remover um recurso | 204 No Content |

No `CustomerController`:
```java
@PostMapping              // POST /api/v1/customers
@GetMapping("/{id}")      // GET  /api/v1/customers/{id}
@GetMapping               // GET  /api/v1/customers
@PutMapping("/{id}")      // PUT  /api/v1/customers/{id}
@DeleteMapping("/{id}")   // DELETE /api/v1/customers/{id}
```

No `OrderController`, o `@PatchMapping` é usado para atualizar apenas o status do pedido, sem enviar o pedido inteiro:
```java
@PatchMapping("/{id}/status")  // PATCH /api/v1/orders/{id}/status
```

**Diferença entre PUT e PATCH:** `PUT` substitui o recurso inteiro (você envia todos os campos). `PATCH` atualiza apenas os campos enviados. No `UpdateCustomerRequest`, os campos são opcionais justamente por isso — é uma operação de patch parcial feita com PUT.

### @RestController

```java
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController { ... }
```

`@RestController` é uma combinação de `@Controller` (registra a classe como controlador MVC) + `@ResponseBody` (serializa automaticamente o retorno dos métodos para JSON). Sem `@ResponseBody`, o Spring tentaria resolver uma view (como um arquivo HTML) em vez de retornar JSON.

`@RequestMapping("/api/v1/customers")` define o caminho base para todos os endpoints desta classe. O prefixo `/api/v1/` é uma convenção de versionamento — se precisar de uma versão incompatível da API no futuro, você cria `/api/v2/customers` sem quebrar os clientes existentes.

### @PathVariable — Parâmetros na URL

```java
@GetMapping("/{id}")
public ApiResponse<CustomerResponse> findById(@PathVariable Long id) { ... }
```

`{id}` na URL é uma variável de template. `@PathVariable` extrai o valor e injeta no parâmetro do método. Ao acessar `GET /api/v1/customers/42`, o parâmetro `id` receberá o valor `42`.

### @RequestParam — Parâmetros de Query

```java
@GetMapping
public ApiResponse<List<CustomerResponse>> findAll(
        @RequestParam(required = false, defaultValue = "false") Boolean activeOnly) { ... }
```

Query params ficam após o `?` na URL: `GET /api/v1/customers?activeOnly=true`. `required = false` torna o parâmetro opcional. `defaultValue = "false"` define o valor padrão quando o parâmetro não é informado.

### @RequestBody — Corpo da Requisição

```java
@PostMapping
public ApiResponse<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) { ... }
```

`@RequestBody` deserializa o corpo JSON da requisição para um objeto Java usando o Jackson (biblioteca de serialização JSON padrão do Spring Boot). O `@Valid` aciona a validação das anotações Bean Validation no objeto.

### @ResponseStatus — Código HTTP da Resposta

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)   // retorna HTTP 201
public ApiResponse<CustomerResponse> create(...) { ... }

@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT) // retorna HTTP 204
public void delete(@PathVariable Long id) { ... }
```

Sem `@ResponseStatus`, o Spring retorna `200 OK` por padrão. `201 Created` é o código semântico correto para criação de recursos. `204 No Content` indica sucesso sem corpo de resposta.

### Versioning de API

O prefixo `/api/v1/` usado nas URLs é uma das estratégias de versionamento:

| Estratégia | Exemplo | Prós/Contras |
|---|---|---|
| **URL path** (usado aqui) | `/api/v1/customers` | Simples, visível, fácil de testar no browser |
| **Query param** | `/customers?version=1` | Sem mudar a URL base, mas menos RESTful |
| **Header** | `Accept-Version: 1` | URL limpa, mas invisível e menos intuitivo |
| **Media type** | `Accept: application/vnd.api.v1+json` | Muito granular, mais complexo |

### Alternativas ao Spring MVC

- **Spring WebFlux:** A alternativa reativa do Spring, usando programação não-bloqueante (Reactor/Project). Ideal para APIs com muita concorrência e chamadas I/O-intensivas (muitos microsserviços se comunicando). Usa `Mono<T>` e `Flux<T>` em vez de retornos síncronos.
- **JAX-RS (Jakarta RESTful Web Services):** Especificação padrão Jakarta EE, implementada por Jersey, RESTEasy. Similar ao Spring MVC mas sem o ecossistema Spring.
- **Ktor (Kotlin):** Framework web assíncrono para Kotlin. Leve e idiomático para Kotlin.
- **Express.js (Node.js):** Referência para quem vem de JavaScript.

---

## 5. Spring Data JPA — Persistência de Dados

### O que é JPA?

**JPA** (Jakarta Persistence API) é uma especificação Java que define como objetos Java devem ser mapeados para tabelas de bancos de dados relacionais — o chamado **ORM** (Object-Relational Mapping). O Hibernate é a implementação JPA mais usada e é o padrão do Spring Boot.

Sem JPA, você escreveria SQL manualmente com JDBC:
```java
// Sem JPA (JDBC puro)
PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers WHERE id = ?");
ps.setLong(1, id);
ResultSet rs = ps.executeQuery();
Customer c = new Customer();
c.setId(rs.getLong("id"));
c.setName(rs.getString("name"));
// ... para cada campo
```

Com JPA, o Hibernate gera esse SQL para você a partir das anotações nas classes Java.

### @Entity e @Table — Mapeando Classes para Tabelas

```java
@Entity
@Table(name = "customers")
public class Customer {
    ...
}
```

`@Entity` marca a classe como uma entidade JPA — o Hibernate a gerenciará e criará a tabela correspondente. `@Table(name = "customers")` especifica o nome da tabela (sem isso, o Hibernate usaria o nome da classe).

### @Id e @GeneratedValue — Chave Primária

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

`@Id` define a chave primária. `@GeneratedValue(strategy = GenerationType.IDENTITY)` delega a geração do ID ao banco de dados (auto increment no MySQL). Alternativas:

| Estratégia | Comportamento |
|---|---|
| `IDENTITY` | Auto increment do banco (MySQL, PostgreSQL serial) |
| `SEQUENCE` | Usa sequence do banco (mais eficiente no PostgreSQL) |
| `TABLE` | Usa uma tabela auxiliar (portável, mas lento) |
| `UUID` | Gerado pela aplicação, sem dependência do banco |

### @Column — Configurações de Coluna

```java
@Column(nullable = false, length = 150)
private String name;

@Column(nullable = false, unique = true, length = 150)
private String email;

@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;
```

- `nullable = false`: gera `NOT NULL` no DDL.
- `unique = true`: gera constraint de unicidade.
- `length = 150`: define o tamanho do `VARCHAR`.
- `name = "created_at"`: define o nome da coluna (snake_case vs camelCase).
- `updatable = false`: impede que o campo seja alterado após a primeira inserção.

### @Enumerated — Persistindo Enums

```java
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private OrderStatus status;
```

`EnumType.STRING` persiste o nome do enum como texto (`"PENDING"`, `"CONFIRMED"` etc.) em vez do índice numérico. **Nunca use `EnumType.ORDINAL`** — se você reordenar os valores do enum, os dados existentes no banco ficam inconsistentes.

```java
public enum OrderStatus {
    PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}
```

### Métodos de Negócio em Entidades — Mantendo a Consistência Bidirecional

```java
// Order.java
public void addItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);  // mantém os dois lados do relacionamento sincronizados
}
```

Em relacionamentos bidirecionais JPA (`@OneToMany` + `@ManyToOne`), você precisa sincronizar ambos os lados:
- Adicionar o `OrderItem` na lista `items` do `Order`.
- Definir o `Order` no campo `order` do `OrderItem`.

Se apenas um lado for atualizado, o Hibernate pode não persistir corretamente o relacionamento. O método `addItem()` encapsula essa dupla responsabilidade, garantindo consistência. No `OrderConverter`, é usado assim:

```java
items.forEach(order::addItem);  // referência de método (Java 8+)
```

Isso é equivalente a `items.forEach(item -> order.addItem(item))`.

### Relacionamentos JPA

#### @OneToMany e @ManyToOne — Um para Muitos

Um `Order` (pedido) possui muitos `OrderItem` (itens). Um `OrderItem` pertence a um único `Order`.

```java
// Lado "um" (Order)
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private List<OrderItem> items = new ArrayList<>();
```

```java
// Lado "muitos" (OrderItem)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "order_id", nullable = false)
private Order order;
```

**Parâmetros importantes:**

- **`mappedBy = "order"`**: Indica que o lado dono do relacionamento é o campo `order` em `OrderItem`. Apenas o lado dono (`@ManyToOne`) possui a coluna de foreign key no banco.
- **`cascade = CascadeType.ALL`**: Operações no `Order` (persist, merge, remove) se propagam automaticamente para os `OrderItem`. Salvar um `Order` salva automaticamente seus itens.
- **`orphanRemoval = true`**: Se um `OrderItem` for removido da lista `items` do `Order`, ele será deletado do banco automaticamente.
- **`fetch = FetchType.LAZY`**: Os itens não são carregados do banco junto com o pedido. Só são buscados quando você acessar `order.getItems()`. Evita carregar dados desnecessários.

**Atenção ao N+1:** `FetchType.EAGER` carrega os filhos junto com o pai sempre, podendo causar N+1 queries (1 query para os pedidos + N queries para os itens de cada pedido). Por isso `LAZY` é preferível, e quando precisar dos dados, use uma query específica com `JOIN FETCH`.

`@JoinColumn(name = "order_id")` define o nome da coluna de foreign key na tabela `order_items`.

### @PrePersist e @PreUpdate — Callbacks de Ciclo de Vida

```java
@PrePersist
void prePersist() {
    createdAt = LocalDateTime.now();
    if (status == null) {
        status = OrderStatus.PENDING;
    }
}

@PreUpdate
void preUpdate() {
    updatedAt = LocalDateTime.now();
}
```

Esses métodos são chamados automaticamente pelo Hibernate antes de persistir ou atualizar a entidade. É o mecanismo ideal para preencher campos de auditoria (`createdAt`, `updatedAt`) sem precisar fazer isso manualmente em cada Service.

Outros callbacks disponíveis: `@PostPersist`, `@PreRemove`, `@PostRemove`, `@PostLoad`, `@PostUpdate`.

**Alternativa:** A anotação `@EntityListeners(AuditingEntityListener.class)` do Spring Data JPA com `@CreatedDate` e `@LastModifiedDate` automatiza isso globalmente, sem precisar adicionar os métodos em cada entidade.

### JpaRepository — Acesso a Dados Sem Implementação

```java
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Customer> findAllByActiveTrue();
}
```

`JpaRepository<Customer, Long>` fornece automaticamente: `save()`, `findById()`, `findAll()`, `deleteById()`, `existsById()`, `count()` e muito mais — sem nenhuma implementação!

Os métodos `findByEmail`, `existsByEmail` e `findAllByActiveTrue` são gerados automaticamente pelo Spring Data a partir do nome do método. É chamado de **Query Derivation** (derivação de consultas): o Spring analisa o nome e gera o SQL equivalente.

| Nome do método | SQL gerado |
|---|---|
| `findByEmail(String email)` | `SELECT * FROM customers WHERE email = ?` |
| `existsByEmail(String email)` | `SELECT COUNT(*) > 0 FROM customers WHERE email = ?` |
| `findAllByActiveTrue()` | `SELECT * FROM customers WHERE active = true` |
| `findAllByStatus(OrderStatus s)` | `SELECT * FROM orders WHERE status = ?` |
| `findAllByCustomerId(Long id)` | `SELECT * FROM orders WHERE customer_id = ?` |

O Spring Data suporta uma linguagem rica de derivação. Outros exemplos de nomes válidos:

| Palavra-chave | Exemplo | SQL gerado |
|---|---|---|
| `And` | `findByNameAndEmail` | `WHERE name = ? AND email = ?` |
| `Or` | `findByStatusOrActive` | `WHERE status = ? OR active = ?` |
| `Between` | `findByCreatedAtBetween` | `WHERE created_at BETWEEN ? AND ?` |
| `Like` | `findByNameLike` | `WHERE name LIKE ?` |
| `Containing` | `findByNameContaining` | `WHERE name LIKE '%?%'` |
| `OrderBy` | `findAllByActiveTrueOrderByName` | `WHERE active = true ORDER BY name` |

### @Query — JPQL para Consultas Complexas

```java
@Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
Optional<Order> findByIdWithItems(Long id);
```

Quando a derivação de nome não é suficiente, você escreve **JPQL** (Java Persistence Query Language). JPQL é parecido com SQL, mas opera sobre entidades Java, não tabelas. `LEFT JOIN FETCH` força o carregamento dos itens junto com o pedido em uma única query, evitando o problema do N+1 em casos onde os itens são necessários.

### ddl-auto — Gerenciamento do Schema

No `application.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

| Valor | Comportamento | Quando usar |
|---|---|---|
| `create` | Cria as tabelas ao subir, destrói ao fechar | Nunca em produção |
| `create-drop` | Igual ao `create`, mas apaga tudo ao encerrar | Testes |
| `update` | Aplica alterações sem destruir dados | Desenvolvimento |
| `validate` | Só valida se o schema está correto | Produção |
| `none` | Não faz nada | Produção (use Flyway/Liquibase) |

**Em produção, use Flyway ou Liquibase** para gerenciar migrações de schema com controle de versão, rollback e rastreabilidade.

### @Transactional — Garantindo Atomicidade

```java
@Transactional
public CustomerResponse create(CreateCustomerRequest request) {
    // Se qualquer coisa falhar aqui, tudo é revertido (rollback)
    Customer customer = CustomerConverter.toEntity(request);
    return CustomerConverter.toResponse(customerRepository.save(customer));
}

@Transactional(readOnly = true)
public CustomerResponse findById(Long id) {
    return customerRepository.findById(id)...
}
```

Uma transação garante que um conjunto de operações seja atômico: ou todas são executadas com sucesso (commit), ou nenhuma é aplicada (rollback).

**`readOnly = true` — Por que usá-lo?** É uma otimização importante em métodos de leitura:

1. O Hibernate desativa o mecanismo de **dirty checking** — ele não rastreia as mudanças de estado das entidades carregadas, economizando tempo e memória.
2. O banco de dados pode direcionar a query para uma réplica de leitura (read replica), reduzindo carga no servidor principal.
3. Garante semanticamente que o método não modifica dados — se alguém tentar chamar `save()` dentro de um método `readOnly`, uma exceção é lançada.

Todos os métodos `find*`, `list*` e `validate*` no projeto usam `readOnly = true`.

### Alternativas ao Spring Data JPA / Hibernate

- **jOOQ:** Gera código Java tipado a partir do schema do banco. Você escreve SQL com fluência em Java, com verificação em tempo de compilação. Excelente para queries complexas.
- **MyBatis:** Framework de mapeamento SQL onde você escreve o SQL manualmente em XML ou anotações. Mais controle, menos magia.
- **JDBC Template (Spring):** Wrapper fino sobre JDBC puro. Elimina o boilerplate do JDBC mas mantém o SQL na sua mão.
- **R2DBC:** Versão reativa do acesso a banco de dados, para uso com Spring WebFlux.

---

## 6. MySQL — Banco de Dados Relacional

### Por que cada serviço tem seu próprio banco?

Este é um princípio fundamental de microsserviços: **Database per Service**. Cada serviço é dono dos seus dados. O `customer-service` não acessa diretamente a tabela `orders`, e o `order-service` não acessa diretamente a tabela `customers`. A comunicação entre eles passa pela API (HTTP).

Isso permite que cada serviço evolua seu schema de forma independente e use o banco de dados mais adequado para sua necessidade (um poderia usar MongoDB, outro PostgreSQL, outro MySQL).

### Configuração no application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:customer_db}?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:root}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

A URL JDBC segue o padrão: `jdbc:<banco>://<host>:<porta>/<banco>?<parâmetros>`. Os parâmetros `${VARIAVEL:valor_padrao}` são referências a variáveis de ambiente com valor padrão — explicados em detalhes na seção de `application.yml`.

### Alternativas ao MySQL

- **PostgreSQL:** Banco relacional open source robusto. Suporte superior a tipos complexos (JSON nativo, arrays, geolocalização). Preferido pela comunidade Java moderna.
- **H2:** Banco em memória, escrito em Java. Excelente para testes, não precisa instalar nada. Neste projeto, os testes usam MySQL via Testcontainers em vez de H2 — escolha deliberada para garantir que os testes reproduzem o ambiente de produção.
- **MariaDB:** Fork open source do MySQL, totalmente compatível mas com licença mais permissiva.
- **SQL Server / Oracle:** Bancos enterprise. Comuns em grandes corporações.
- **MongoDB:** Banco NoSQL orientado a documentos. Sem schema fixo, ideal para dados com estrutura variável.

---

## 7. Lombok — Eliminando Código Repetitivo

### O problema que o Lombok resolve

Em Java, para criar um simples DTO (objeto de transferência de dados) com 5 campos, você precisaria escrever manualmente: construtores, getters, setters, `equals()`, `hashCode()`, `toString()`. São dezenas de linhas para cada classe.

O Lombok é um **processador de anotações** que gera esse código automaticamente em tempo de compilação — ele não existe em runtime, por isso é excluído do fat JAR.

### @Data

```java
@Data
public class CustomerResponse {
    private Long id;
    private String name;
    // ...
}
```

`@Data` gera: `getters` para todos os campos, `setters` para campos não-finais, `equals()` e `hashCode()` usando todos os campos, `toString()` com todos os campos, e um construtor com todos os campos obrigatórios.

### @Builder — Padrão Builder

```java
@Builder
public class CreateCustomerRequest {
    private String name;
    private String email;
}

// Uso:
CreateCustomerRequest request = CreateCustomerRequest.builder()
        .name("João Silva")
        .email("joao@email.com")
        .build();
```

O padrão Builder cria objetos com clareza, especialmente quando há muitos campos opcionais. Sem Lombok, você implementaria a classe `CreateCustomerRequest.CreateCustomerRequestBuilder` manualmente.

**`@Builder.Default`**: Quando usa `@Builder`, os valores padrão dos campos são ignorados. `@Builder.Default` preserve o valor padrão:

```java
@Builder.Default
private Boolean active = true;  // permanecerá true mesmo com builder
```

### @NoArgsConstructor e @AllArgsConstructor

```java
@NoArgsConstructor  // gera: public CreateCustomerRequest() {}
@AllArgsConstructor // gera: public CreateCustomerRequest(String name, String email, String phone) {}
```

JPA exige que as entidades tenham um construtor sem argumentos (para instanciar objetos ao carregar do banco). Jackson também precisa do construtor sem argumentos para desserializar JSON. `@AllArgsConstructor` é usado junto com `@Builder` para permitir a criação de objetos imutáveis.

### @RequiredArgsConstructor — Injeção de Dependência

```java
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository; // campo final = obrigatório
}
```

Gera um construtor com todos os campos `final` e `@NonNull`. Combinado com injeção por construtor do Spring, é o padrão recomendado para injeção de dependências.

### @Slf4j — Logging

```java
@Slf4j
public class CustomerServiceImpl {
    // Gera: private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    public CustomerResponse create(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());
        log.warn("Aviso importante: {}", mensagem);
        log.error("Erro crítico: {}", ex.getMessage(), ex);
        log.debug("Detalhe de debug: {}", detalhe);
    }
}
```

O `{}` é um placeholder para interpolação de strings. Isso é mais eficiente do que concatenação de strings (`"Creating: " + email`) porque a string só é construída se o nível de log estiver ativo.

### @EqualsAndHashCode — Controle de Igualdade

```java
@EqualsAndHashCode(of = "id")
public class Customer {
    private Long id;
    private String name;
    // ...
}
```

Gera `equals()` e `hashCode()` usando apenas o campo `id`. Dois `Customer` são iguais se e somente se têm o mesmo `id`. Usar `@Data` sem `@EqualsAndHashCode(of = "id")` numa entidade JPA é perigoso: a comparação por todos os campos pode causar ciclos infinitos em relacionamentos bidirecionais e comportamentos incorretos em coleções.

### @ToString(exclude = "order")

```java
@ToString(exclude = "order")
public class OrderItem {
    private Order order; // excluído do toString
}
```

Em relacionamentos bidirecionais (`Order` contém `List<OrderItem>`, `OrderItem` contém `Order`), sem `exclude`, o `toString()` entraria em loop infinito (StackOverflowError).

### Alternativas ao Lombok

- **Records (Java 16+):** Para DTOs imutáveis, o Java oferece o tipo `record`:
  ```java
  public record CustomerResponse(Long id, String name, String email) {}
  ```
  Gera automaticamente: construtor, getters, `equals`, `hashCode`, `toString`. Porém, `record` é imutável — não tem setters, o que pode ser um problema com frameworks que precisam de mutabilidade (como Hibernate).
- **Kotlin data class:** Se usar Kotlin, `data class` oferece funcionalidade equivalente ao `@Data` do Lombok nativamente.
- **Escrever manualmente:** Em equipes que evitam ferramentas de processamento de anotações ou ambientes com problemas de compatibilidade de IDE.

---

## 8. Bean Validation — Validação de Dados de Entrada

### O problema

Antes de processar um cadastro de cliente, você precisa garantir que o nome não está vazio, que o e-mail tem formato válido etc. Sem um framework, você escreveria isso manualmente em cada Service — código repetitivo e difícil de manter.

O **Bean Validation** (especificação Jakarta EE) define anotações declarativas de validação que o Spring valida automaticamente antes de chamar o método do controller.

### Anotações de Validação

```java
public class CreateCustomerRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Size(max = 20, message = "Phone must have at most 20 characters")
    private String phone;
}
```

| Anotação | Valida |
|---|---|
| `@NotNull` | Não é `null` |
| `@NotBlank` | Não é `null`, não é vazio e não é só espaços |
| `@NotEmpty` | Não é `null` e não é vazio (para String, Collection, array) |
| `@Email` | Formato de e-mail válido |
| `@Size(min, max)` | Tamanho dentro do intervalo |
| `@Min(value)` | Número >= value |
| `@Max(value)` | Número <= value |
| `@DecimalMin(value)` | Decimal >= value |
| `@DecimalMax(value)` | Decimal <= value |
| `@Pattern(regexp)` | String corresponde à regex |
| `@Positive` | Número positivo |
| `@Past` / `@Future` | Data no passado/futuro |

### Como a validação é ativada

```java
@PostMapping
public ApiResponse<CustomerResponse> create(
        @Valid @RequestBody CreateCustomerRequest request) {
    // Só chega aqui se o request for válido
}
```

`@Valid` ativa a validação do objeto. Se alguma regra for violada, o Spring lança `MethodArgumentNotValidException`, que é capturada pelo `GlobalExceptionHandler`:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
public ApiResponse<Void> handleValidation(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)  // usa a "message" da anotação
            .collect(Collectors.toList());
    return ApiResponse.error("Validation failed", errors);
}
```

### Validação em cascata com @Valid

```java
public class CreateOrderRequest {
    @NotNull
    private Long customerId;

    @NotEmpty
    @Valid   // ← valida cada item da lista
    private List<CreateOrderItemRequest> items;
}
```

O `@Valid` dentro de `CreateOrderRequest` garante que cada `CreateOrderItemRequest` da lista também seja validado com suas próprias anotações (`@NotBlank`, `@Min`, `@DecimalMin`).

### Alternativas ao Bean Validation

- **Validação manual no Service:** Mais controle, mas verboso.
- **Fluent Validation (padrão em .NET):** Equivalente no Java seria usar a biblioteca [Vavr](https://www.vavr.io/) ou escrever uma cadeia de chamadas.
- **Apache Commons Validator:** Validações utilitárias sem anotações.
- **Custom `@Constraint`:** O Bean Validation permite criar suas próprias anotações de validação para regras de negócio complexas.

---

## 9. OpenFeign — Comunicação Entre Microsserviços

### O problema da comunicação entre serviços

O `order-service` precisa verificar se o cliente existe e está ativo antes de criar um pedido. Como o `customer-service` está em outro processo (outra porta, possivelmente outro servidor), essa comunicação deve ocorrer via HTTP.

Usando `RestTemplate` ou `HttpClient` puro, você escreveria:
```java
// Sem Feign (verboso e propenso a erros)
RestTemplate rest = new RestTemplate();
String url = "http://localhost:8081/api/v1/customers/" + id + "/validate-active";
ResponseEntity<ApiResponse<CustomerDTO>> response = rest.getForEntity(url, ...);
```

**OpenFeign** resolve isso com uma interface declarativa — você define como a chamada deve ser, e o Feign gera a implementação HTTP em tempo de execução.

### @FeignClient — Interface Declarativa

```java
@FeignClient(
        name = "customer-service",
        url = "${customer-service.url}",
        configuration = FeignConfig.class
)
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}/validate-active")
    ApiResponse<CustomerDTO> validateActiveCustomer(@PathVariable Long id);
}
```

- `name`: Nome lógico do serviço (usado em logs e para Service Discovery se houver).
- `url`: URL base do serviço, lida do `application.yml`.
- `configuration`: Classe de configuração customizada (neste caso, o `ErrorDecoder`).

O método `validateActiveCustomer` usa as mesmas anotações Spring MVC (`@GetMapping`, `@PathVariable`) que você usaria no controller. O Feign traduz isso em uma chamada HTTP GET.

### FeignConfig — Tratamento de Erros do Cliente HTTP

```java
@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomerFeignErrorDecoder();
    }

    static class CustomerFeignErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            return switch (response.status()) {
                case 404 -> new FeignIntegrationException("Customer not found");
                case 422 -> new FeignIntegrationException("Customer is not active");
                case 503 -> new FeignIntegrationException("Customer service is unavailable");
                default -> defaultDecoder.decode(methodKey, response);
            };
        }
    }
}
```

Sem um `ErrorDecoder` customizado, o Feign lança `FeignException` com o status HTTP como código. Com o `ErrorDecoder`, você converte os erros HTTP em exceções de domínio significativas (`FeignIntegrationException`), que são capturadas pelo `GlobalExceptionHandler` e retornadas ao cliente com uma mensagem clara.

O `switch` aqui usa a **switch expression** do Java 14+ — retorna um valor diretamente sem precisar de `break`.

### @EnableFeignClients — Ativando o Feign no Módulo

```java
@SpringBootApplication(scanBasePackages = "com.imepac")
@EnableFeignClients(basePackages = "com.imepac.order.client")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

`@EnableFeignClients` é **obrigatório** para que o Spring crie as implementações das interfaces `@FeignClient`. Sem essa anotação, o `CustomerClient` não seria encontrado no contexto Spring e a injeção de dependência falharia ao subir a aplicação.

O parâmetro `basePackages = "com.imepac.order.client"` restringe o scan: o Spring só procura interfaces `@FeignClient` dentro do pacote `com.imepac.order.client`. Isso é mais eficiente do que escanear todos os subpacotes de `com.imepac`.

**Por que está no `OrderServiceApplication` e não no `customer-service`?** Porque apenas o `order-service` faz chamadas HTTP para outros serviços. O `customer-service` apenas recebe chamadas.

### URL configurável via application.yml

```yaml
# order-service/src/main/resources/application.yml
customer-service:
  url: ${CUSTOMER_SERVICE_URL:http://localhost:8081}
```

Em desenvolvimento: `http://localhost:8081`. Em produção (Docker Compose): `http://customer-service:8081` (usando o nome do container como hostname). Isso é injetado via variável de ambiente `CUSTOMER_SERVICE_URL`.

### Service Discovery (alternativa ao URL fixo)

Em produção com muitas instâncias, a URL fixa não escala. Ferramentas de **Service Discovery** como **Consul** ou **Eureka** (Spring Cloud Netflix) permitem que os serviços se registrem e o Feign encontre a URL correta dinamicamente, com balanceamento de carga automático.

### Alternativas ao OpenFeign

- **Spring WebClient (Reactive):** Cliente HTTP reativo do Spring WebFlux. Preferível quando você precisa de operações não-bloqueantes.
- **RestTemplate:** O cliente HTTP clássico do Spring (declarado como legado no Spring 5+). Síncrono e imperativo.
- **HttpClient (Java 11+):** Cliente HTTP nativo do Java, tanto síncrono quanto assíncrono.
- **OkHttp:** Cliente HTTP muito popular para Android e Java, também usado pelo Feign por baixo.
- **gRPC:** Protocolo binário de alto desempenho do Google. Alternativa ao REST/HTTP para comunicação interna entre serviços onde performance é crítica.

---

## 10. OpenAPI / Swagger — Documentação da API

### O problema

Como um desenvolvedor frontend, mobile ou um colega de equipe descobre quais endpoints existem, quais parâmetros aceitar e qual JSON enviar? Sem documentação, ele precisaria ler o código ou testar no escuro.

**OpenAPI** (formalmente Swagger) é uma especificação para descrever APIs REST. O **SpringDoc** integra o Spring Boot com OpenAPI, gerando automaticamente a documentação a partir do código.

### SwaggerConfig — Configurando os Metadados

```java
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Service API")
                        .description("REST API for customer management")
                        .version("v1")
                        .contact(new Contact()
                                .name("IMEPAC")
                                .email("contato@imepac.edu.br")));
    }
}
```

`@Configuration` e `@Bean` são anotações do Spring: `@Configuration` marca a classe como fonte de Beans, e `@Bean` marca o método que retorna um objeto que deve ser gerenciado pelo Spring. O Spring chama esse método uma vez e mantém a instância no contexto.

### Anotações no Controller

```java
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

    @Operation(summary = "Create a new customer")
    public ApiResponse<CustomerResponse> create(...) {}

    @Parameter(description = "Customer ID") @PathVariable Long id
}
```

- `@Tag`: Agrupa endpoints relacionados na UI do Swagger.
- `@Operation(summary)`: Descrição curta do endpoint.
- `@Parameter`: Documenta um parâmetro específico.

### Configuração no application.yml

```yaml
springdoc:
  api-docs:
    path: /api-docs       # JSON da especificação OpenAPI
  swagger-ui:
    path: /swagger-ui.html  # Interface gráfica interativa
    operationsSorter: method  # ordena por método HTTP
```

Após subir os serviços, acesse:
- `http://localhost:8081/swagger-ui.html` — interface interativa do customer-service.
- `http://localhost:8082/swagger-ui.html` — interface interativa do order-service.

Na interface do Swagger UI, você pode visualizar todos os endpoints, ver os schemas de request/response e até executar chamadas HTTP diretamente pelo browser.

### Por que documentar a API?

- **Contrato:** Frontend e backend podem trabalhar em paralelo a partir da especificação.
- **Onboarding:** Novos desenvolvedores entendem a API sem ler o código.
- **Geração de código:** A especificação OpenAPI pode gerar clientes (SDKs) em qualquer linguagem.
- **Testes:** Ferramentas como Postman importam a especificação OpenAPI automaticamente.

### Alternativas ao SpringDoc

- **Springfox:** Biblioteca anterior ao SpringDoc. Incompatível com Spring Boot 3. Use SpringDoc.
- **Redoc:** UI alternativa ao Swagger UI, mais limpa visualmente.
- **Stoplight / Postman:** Ferramentas de design-first (você escreve a especificação primeiro, depois implementa).

---

## 11. Logbook — Logging de Requisições HTTP

### O que é o Logbook?

**Logbook** (da Zalando) é uma biblioteca Spring Boot que registra automaticamente as requisições e respostas HTTP completas nos logs da aplicação. Essencial para debugging e auditoria em APIs.

### Configuração

```yaml
logbook:
  filter:
    enabled: true
  format:
    style: http       # formato legível de HTTP (alternativa: json)
  include:
    - /api/v1/**      # só loga endpoints da API (ignora /swagger-ui, /actuator etc.)
  obfuscate:
    headers:
      - Authorization # oculta o header de autenticação nos logs
```

Com isso, cada requisição e resposta HTTP que passar pelos endpoints `/api/v1/**` é registrada automaticamente no log, incluindo método, URL, headers, corpo da requisição e corpo da resposta.

### Por que obfuscar o header Authorization?

Tokens de autenticação (JWT, API Keys) não devem aparecer nos logs porque:
1. Os logs podem ser armazenados em sistemas de log centralizado acessíveis por muitas pessoas.
2. Tokens vazados permitem que atacantes se façam passar por usuários legítimos.

### Alternativas ao Logbook

- **Filtros customizados (`OncePerRequestFilter`):** Você implementa a lógica de logging manualmente com acesso total ao request/response.
- **Spring Boot Actuator + Micrometer:** Para métricas (contagem de requests, latência) em vez de logs detalhados. Integra com Prometheus e Grafana.
- **ELK Stack (Elasticsearch, Logstash, Kibana):** Para centralização e análise de logs em escala.
- **Interceptors do Feign:** Para logar as chamadas de saída (outbound HTTP) do Feign.

---

## 12. Padrões de Projeto e Arquitetura em Camadas

### A Arquitetura Usada

Este projeto segue a arquitetura em camadas clássica de aplicações Spring:

```
HTTP Request
     ↓
 Controller         ← recebe a requisição, valida, delega
     ↓
 Service Interface  ← contrato: define o que o serviço faz
     ↓
 ServiceImpl        ← implementação: como o serviço faz
     ↓
 Repository         ← acesso a dados
     ↓
 Database
```

### Controller — A Camada de Apresentação

Responsável por:
- Receber requisições HTTP.
- Validar entrada (`@Valid`).
- Delegar para o Service.
- Formatar e retornar a resposta.

O Controller **não contém lógica de negócio**. Ele é um roteador.

### Service Interface + Impl — A Camada de Negócio

```java
// Interface: define o contrato
public interface CustomerService {
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse findById(Long id);
    ...
}

// Implementação: contém a lógica de negócio
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public CustomerResponse create(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer already exists with email: " + request.getEmail());
        }
        // ...
    }
}
```

**Por que usar Interface + Impl?**

1. **Inversão de Dependência (SOLID - D):** O Controller depende da interface, não da implementação. Isso facilita trocar a implementação sem mudar o Controller.
2. **Testabilidade:** Em testes unitários, você injeta um mock da interface, não precisa saber nada sobre a implementação.
3. **Proxies do Spring:** Funcionalidades como `@Transactional` e AOP (Programação Orientada a Aspectos) funcionam através de proxies que o Spring cria para interfaces.

### DTO Pattern — Separação de Camadas de Dados

O projeto usa três tipos de objetos para representar dados:

| Tipo | Exemplo | Propósito |
|---|---|---|
| **Entity** | `Customer` | Representa uma linha do banco de dados. Gerenciada pelo JPA. |
| **Request DTO** | `CreateCustomerRequest` | Dados que chegam do cliente. Contém validações. |
| **Response DTO** | `CustomerResponse` | Dados enviados ao cliente. Pode omitir campos sensíveis. |
| **Shared DTO** | `CustomerDTO` (commons) | Contrato entre microsserviços. |

Por que não retornar a `Entity` diretamente? Porque:
1. A Entity pode ter campos sensíveis (senha, dados internos) que não devem ser expostos.
2. A Entity tem anotações JPA que podem causar problemas de serialização (loops em relacionamentos lazy, campos desnecessários).
3. A Entity é amarrada ao schema do banco; o DTO pode ter uma forma diferente, mais conveniente para o cliente.

### Converter Pattern

```java
public final class CustomerConverter {

    private CustomerConverter() {} // impede instanciação — classe utilitária

    public static Customer toEntity(CreateCustomerRequest request) { ... }
    public static CustomerResponse toResponse(Customer customer) { ... }
    public static CustomerDTO toDTO(Customer customer) { ... } // para comunicação entre serviços
}
```

A classe `Converter` centraliza a conversão entre as camadas. O construtor privado impede que alguém instancie a classe — é uma classe utilitária com métodos estáticos.

O `OrderConverter` é mais complexo — converte pedido e itens, e calcula o `totalAmount`:

```java
public static Order toEntity(CreateOrderRequest request) {
    Order order = Order.builder()
            .customerId(request.getCustomerId())
            .status(OrderStatus.PENDING)
            .totalAmount(BigDecimal.ZERO)
            .build();

    List<OrderItem> items = request.getItems().stream()
            .map(itemReq -> toItemEntity(itemReq, order))
            .collect(Collectors.toList());

    items.forEach(order::addItem); // referência de método — sincroniza o relacionamento bidirecional

    BigDecimal total = items.stream()
            .map(OrderItem::getSubtotal)               // getSubtotal() = unitPrice * quantity
            .reduce(BigDecimal.ZERO, BigDecimal::add); // soma todos os subtotais

    order.setTotalAmount(total);
    return order;
}
```

O `totalAmount` é calculado na criação e persistido. Isso evita recalculá-lo a cada leitura e garante que o valor reflita o momento da compra, mesmo que os preços mudem depois.

**Alternativa:** A biblioteca **MapStruct** gera código de mapeamento automaticamente a partir de interfaces anotadas, eliminando esse código manual e com performance superior por ser gerado em tempo de compilação.

### ApiResponse<T> — Padrão de Envelope de Resposta

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;
}
```

Todas as respostas da API têm o mesmo formato (envelope), independente do endpoint. Isso facilita para o cliente da API tratar respostas de forma uniforme.

```json
// Sucesso
{"success": true, "data": {"id": 1, "name": "João"}, "timestamp": "2024-01-01T10:00:00"}

// Erro de validação
{"success": false, "message": "Validation failed", "errors": ["Name is required"], "timestamp": "..."}
```

`@JsonInclude(JsonInclude.Include.NON_NULL)`: campos com valor `null` não são incluídos no JSON, mantendo a resposta limpa. Um sucesso sem `message` não terá o campo `message: null` no JSON.

O uso de **Generics** (`<T>`) permite que `ApiResponse` seja reutilizada para qualquer tipo de dado: `ApiResponse<CustomerResponse>`, `ApiResponse<List<OrderResponse>>`, `ApiResponse<Void>`.

### Update DTOs — Atualização Parcial de Recursos

```java
public class UpdateCustomerRequest {
    @Size(min = 2, max = 150)
    private String name;   // opcional

    @Size(max = 20)
    private String phone;  // opcional

    private Boolean active; // opcional
}
```

Em `UpdateCustomerRequest`, **todos os campos são opcionais** (sem `@NotBlank`/`@NotNull`). O service aplica apenas os campos não-nulos:

```java
if (request.getName() != null) {
    customer.setName(request.getName());
}
if (request.getPhone() != null) {
    customer.setPhone(request.getPhone());
}
```

Essa abordagem permite que o cliente envie apenas os campos que quer modificar sem precisar enviar o objeto completo. A diferença semântica entre `PUT` e `PATCH`:

- **`PUT` com corpo parcial** (usado neste projeto): Formalmente incorreto segundo o padrão REST puro (PUT deveria substituir o recurso inteiro), mas amplamente aceito na prática.
- **`PATCH` verdadeiro**: O cliente envia somente os campos alterados. É o verbo HTTP semanticamente correto. O `OrderController` usa `@PatchMapping` para atualizar apenas o status do pedido — o uso mais correto do verbo.

`UpdateOrderStatusRequest` é ainda mais restrito — aceita apenas o campo `status` com `@NotNull`, pois atualizar apenas o status é a única operação de atualização permitida em pedidos.

### Repository Pattern

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> { ... }
```

`@Repository` é um marcador semântico que:
1. Indica ao Spring que essa interface deve ser detectada pelo component scan.
2. Ativa a tradução automática de exceções específicas do banco (como `DataIntegrityViolationException`) para a hierarquia de exceções do Spring.

O padrão Repository abstrai o acesso a dados — o Service não sabe se os dados vêm de MySQL, MongoDB ou de um arquivo. Isso facilita a troca de tecnologia de persistência.

---

## 13. Tratamento Global de Exceções

### O problema sem um handler global

Sem tratamento centralizado de exceções, cada Controller teria blocos `try-catch` repetidos, e exceções não capturadas retornariam stack traces para o cliente — o que é um risco de segurança (vaza informações internas).

### @RestControllerAdvice — O Interceptador Global

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    public ApiResponse<Void> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    public ApiResponse<Void> handleBusiness(BusinessException ex) { ... }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    public ApiResponse<Void> handleGeneral(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ApiResponse.error("An unexpected error occurred");
    }
}
```

`@RestControllerAdvice` combina `@ControllerAdvice` (intercepta exceções de qualquer Controller no contexto) com `@ResponseBody` (serializa a resposta para JSON). Funciona como um "catch global" para toda a aplicação.

`@ExceptionHandler(TipoDeExcecao.class)` define qual tipo de exceção o método trata. O Spring escolhe o handler mais específico para cada exceção.

### Hierarquia de Exceções Customizadas

```
RuntimeException
└── EntityNotFoundException     → HTTP 404 (Customer/Order not found)
└── BusinessException          → HTTP 422 (regra de negócio violada)
└── FeignIntegrationException  → HTTP 502 (falha na comunicação entre serviços)
```

**`FeignIntegrationException` vs `FeignException`:** O `GlobalExceptionHandler` trata ambas:

```java
@ExceptionHandler(FeignIntegrationException.class)
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public ApiResponse<Void> handleFeignIntegration(FeignIntegrationException ex) { ... }

@ExceptionHandler(FeignException.class)
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public ApiResponse<Void> handleFeignException(FeignException ex) { ... }
```

- **`FeignException`**: Exceção nativa da biblioteca Feign, lançada quando ocorre um erro HTTP não tratado pelo `ErrorDecoder`. Contém o status HTTP e o corpo da resposta do serviço remoto como bytes.
- **`FeignIntegrationException`**: Exceção de domínio **deste projeto**, lançada pelo `CustomerFeignErrorDecoder` para erros HTTP específicos (404, 422, 503). É semanticamente mais rica — diz exatamente o que aconteceu de negócio.

A ordem importa: o Spring usa o handler mais específico. Como `FeignIntegrationException` não herda de `FeignException`, os dois handlers são necessários: o primeiro para os erros tratados pelo `ErrorDecoder`, o segundo como fallback para erros não mapeados.

Criar exceções específicas para cada tipo de erro de domínio melhora:
- **Semântica:** `CustomerNotFoundException` é mais expressivo que `RuntimeException`.
- **Tratamento:** O handler global mapeia cada exceção para o status HTTP correto.
- **Manutenibilidade:** Para adicionar novo comportamento para `CustomerNotFoundException`, você muda apenas o handler.

```java
// Em CustomerNotFoundException:
public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException(Long id) {
        super("Customer", id);  // "Customer not found with id: 42"
    }
}
```

### Hierarquia de Status HTTP Usados

| Status | Código | Quando |
|---|---|---|
| OK | 200 | Operação bem-sucedida com retorno |
| Created | 201 | Recurso criado com sucesso |
| No Content | 204 | Operação bem-sucedida sem retorno (DELETE) |
| Bad Request | 400 | Validação falhou (dados inválidos) |
| Not Found | 404 | Recurso não encontrado |
| Unprocessable Entity | 422 | Dados válidos mas regra de negócio violada |
| Bad Gateway | 502 | Erro na comunicação com outro serviço |
| Internal Server Error | 500 | Erro inesperado do servidor |

---

## 14. Configuração com application.yml

### YAML vs properties

O Spring Boot suporta dois formatos de configuração:

```yaml
# application.yml (YAML)
spring:
  application:
    name: customer-service
  datasource:
    url: jdbc:mysql://localhost:3306/customer_db
```

```properties
# application.properties (Properties)
spring.application.name=customer-service
spring.datasource.url=jdbc:mysql://localhost:3306/customer_db
```

YAML é mais legível para configurações hierárquicas e é o padrão usado neste projeto.

### Variáveis de Ambiente com Valores Padrão

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:customer_db}?...
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:root}
```

O padrão `${VARIAVEL:valor_padrao}` é uma das funcionalidades mais importantes para 12-factor apps:

- **Em desenvolvimento:** A variável de ambiente não existe → usa o valor padrão (`localhost`, `root`).
- **Em produção (Docker Compose):** A variável de ambiente é definida no `docker-compose.yml` → usa o valor real.

Isso permite que o mesmo artefato (JAR ou container) funcione em todos os ambientes sem alteração.

### Configuração por Perfil (application-test.yml)

```yaml
# application-test.yml — ativo com @ActiveProfiles("test")
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop  # recria o schema a cada teste
    show-sql: true           # mostra o SQL nos logs (útil para debug)
```

O Spring Boot carrega `application.yml` sempre, e sobrescreve/complementa com `application-{perfil}.yml` quando o perfil está ativo. Nos testes de integração, o perfil `test` está ativo via `@ActiveProfiles("test")`.

### Configurações Importantes

```yaml
server:
  port: 8081  # porta do servidor HTTP

spring:
  application:
    name: customer-service  # nome usado em logs e Service Discovery

logging:
  level:
    com.imepac: DEBUG        # logs DEBUG para o código do projeto
    org.zalando.logbook: TRACE  # logs detalhados do Logbook
```

Os níveis de log em ordem crescente de severidade: `TRACE < DEBUG < INFO < WARN < ERROR`. Em produção, use `INFO` ou `WARN`. Em desenvolvimento/testes, `DEBUG` é útil.

---

## 15. Spring Auto-configuration — Módulo Commons Compartilhado

### O problema de compartilhar código entre microsserviços

O `GlobalExceptionHandler`, as exceções base e os DTOs compartilhados precisam estar disponíveis em ambos os serviços. Em vez de duplicar esse código, eles vivem no módulo `commons`, que é incluído como dependência.

Mas existe um desafio: o `@ComponentScan` do Spring Boot só varre automaticamente o pacote da classe principal e seus subpacotes. O `GlobalExceptionHandler` está em `com.imepac.commons.handler`, mas o `customer-service` está em `com.imepac.customer`. O Spring não encontraria o handler automaticamente.

### A solução: Spring Boot Auto-configuration

```java
// commons/src/main/java/com/imepac/commons/config/CommonsAutoConfiguration.java
@AutoConfiguration
public class CommonsAutoConfiguration {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
```

```
# commons/src/main/resources/META-INF/spring/
# org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.imepac.commons.config.CommonsAutoConfiguration
```

O arquivo `AutoConfiguration.imports` é o **Service Provider Interface (SPI)** do Spring Boot: quando qualquer aplicação inclui o `commons` como dependência, o Spring Boot lê esse arquivo e carrega automaticamente a `CommonsAutoConfiguration`, que registra o `GlobalExceptionHandler` como Bean — sem que o desenvolvedor do serviço precise configurar nada.

É exatamente assim que os próprios starters do Spring Boot funcionam: quando você adiciona `spring-boot-starter-data-jpa`, ele tem seu próprio arquivo de auto-configuração que configura o EntityManager, TransactionManager etc. automaticamente.

### scanBasePackages — A Alternativa Usada no Projeto

```java
@SpringBootApplication(scanBasePackages = "com.imepac")
```

Esta é uma alternativa mais simples: diz ao Spring para varrer todo o pacote `com.imepac` e seus subpacotes, encontrando as classes do `commons` automaticamente. A auto-configuration via `AutoConfiguration.imports` é a abordagem mais robusta (usada pelos starters profissionais), enquanto `scanBasePackages` é mais simples e direta.

---

## 16. Docker — Containerização

### O problema sem containers

"Funciona na minha máquina!" é um problema clássico do desenvolvimento de software. A aplicação funciona no computador do desenvolvedor (Java 17, MySQL 8) mas falha no servidor de produção (Java 11, MySQL 5.7). Docker resolve isso empacotando a aplicação com todas as suas dependências em um container isolado.

### O que é um container Docker?

Um container é um processo isolado que roda sobre o kernel do sistema operacional hospedeiro, mas com seu próprio sistema de arquivos, rede e recursos. É mais leve que uma VM (não virtualiza o hardware), mais rápido de iniciar e reproduzível.

Uma **imagem Docker** é um template imutável para criar containers. O `Dockerfile` define como construir essa imagem.

### Dockerfile do customer-service

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/customer-service-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Linha por linha:

- **`FROM eclipse-temurin:17-jre-alpine`**: Imagem base. `eclipse-temurin` é a distribuição OpenJDK da Fundação Eclipse. `17` é a versão do Java. `jre` é o Java Runtime (sem compilador, menor que o JDK). `alpine` é uma distribuição Linux minimalista (~5MB), tornando a imagem final muito menor.
- **`WORKDIR /app`**: Define o diretório de trabalho dentro do container.
- **`ARG JAR_FILE=...`**: Argumento de build passado pelo docker-compose (`args: JAR_FILE: target/customer-service-1.0.0.jar`).
- **`COPY ${JAR_FILE} app.jar`**: Copia o JAR para dentro da imagem.
- **`EXPOSE 8081`**: Documenta que o container usa a porta 8081 (não publica automaticamente — o `ports` do docker-compose faz isso).
- **`ENTRYPOINT ["java", "-jar", "app.jar"]`**: Comando executado quando o container inicia.

### Multi-stage Build (boa prática não usada neste projeto)

O Dockerfile atual requer que o JAR já esteja compilado antes do build da imagem. Uma alternativa mais robusta é o **multi-stage build**:

```dockerfile
# Estágio 1: Compilação
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/customer-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

O multi-stage build garante que a imagem final contenha apenas o runtime — sem o Maven, sem o código-fonte, sem arquivos intermediários de compilação. A imagem resultante é menor e mais segura.

### Alternativas ao Docker

- **Podman:** Alternativa open source ao Docker, sem daemon root, compatível com os mesmos Dockerfiles e imagens.
- **Buildpacks (Cloud Native Buildpacks):** Constrói imagens OCI sem Dockerfile. O Spring Boot suporta nativamente: `mvn spring-boot:build-image`. Aplica boas práticas automaticamente.
- **GraalVM Native Image:** Compila a aplicação para um executável nativo (sem JVM). Tempo de inicialização em milissegundos, consumo de memória drasticamente menor. Funciona com Spring Boot 3 (AOT).

---

## 17. Docker Compose — Orquestração de Containers

### O que é o Docker Compose?

O Docker Compose define e gerencia múltiplos containers em um único arquivo `docker-compose.yml`. Em vez de iniciar cada container manualmente com `docker run`, você descreve toda a infraestrutura e sobe tudo com `docker-compose up`.

### Análise do docker-compose.yml

#### Serviços de Banco de Dados

```yaml
mysql-customer:
  image: mysql:8.0
  container_name: mysql-customer
  environment:
    MYSQL_ROOT_PASSWORD: root
    MYSQL_DATABASE: customer_db
  ports:
    - "3307:3306"   # host:container — porta 3307 no host mapeia para 3306 no container
  volumes:
    - mysql_customer_data:/var/lib/mysql  # persistência de dados
  networks:
    - order-system-network
  healthcheck:
    test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
    interval: 10s   # testa a cada 10 segundos
    timeout: 5s     # aguarda no máximo 5 segundos pela resposta
    retries: 5      # falha após 5 tentativas consecutivas
    start_period: 30s  # período de tolerância inicial (o MySQL demora para iniciar)
```

**Por que portas diferentes (3307 e 3308) para os bancos?** Para evitar conflito no host: os dois containers MySQL internamente usam a porta 3306, mas são mapeados para portas diferentes no host para permitir acesso externo simultâneo (ex: via DBeaver).

**Volumes nomeados** (`mysql_customer_data`) persistem os dados do banco fora do container. Sem isso, ao recriar o container, os dados seriam perdidos.

#### Serviços da Aplicação

```yaml
customer-service:
  build:
    context: customer-service     # diretório do Dockerfile
    dockerfile: Dockerfile
    args:
      JAR_FILE: target/customer-service-1.0.0.jar
  image: imepac/customer-service:1.0.0
  ports:
    - "8081:8081"
  environment:
    DB_HOST: mysql-customer       # nome do serviço do banco = hostname na rede Docker
    DB_PORT: 3306
    DB_NAME: customer_db
    DB_USER: root
    DB_PASSWORD: root
  networks:
    - order-system-network
  depends_on:
    mysql-customer:
      condition: service_healthy  # só sobe após o banco estar saudável
  restart: on-failure             # reinicia automaticamente em caso de falha
```

**O DNS interno do Docker:** Dentro da rede `order-system-network`, os containers se comunicam pelo nome do serviço. `DB_HOST: mysql-customer` funciona porque o Docker Compose cria registros DNS para cada serviço na rede interna.

**`depends_on` com `condition: service_healthy`:** O `customer-service` só sobe após o `mysql-customer` responder positivamente ao healthcheck. Sem isso, o serviço poderia tentar conectar ao banco antes de ele estar pronto.

```yaml
order-service:
  environment:
    CUSTOMER_SERVICE_URL: http://customer-service:8081
```

O `order-service` usa `http://customer-service:8081` para se comunicar com o `customer-service` — usando o DNS interno do Docker.

#### Redes e Volumes

```yaml
networks:
  order-system-network:
    driver: bridge   # rede virtual isolada do host

volumes:
  mysql_customer_data:   # volume gerenciado pelo Docker
  mysql_order_data:
```

A rede `bridge` cria uma rede virtual privada. Containers na mesma rede se comunicam entre si mas estão isolados do host e de outras redes Docker. Containers em redes diferentes não se comunicam (isolamento de segurança).

### Alternativas ao Docker Compose

- **Kubernetes (K8s):** Para orquestração em produção com múltiplos hosts, auto-scaling, auto-healing, rolling deployments. O Docker Compose é para desenvolvimento/testes locais; Kubernetes é para produção.
- **Docker Swarm:** Orquestração nativa do Docker, mais simples que Kubernetes mas com menos funcionalidades.
- **Helm:** Gerenciador de pacotes para Kubernetes. Define aplicações Kubernetes como "charts" reutilizáveis.

---

## 18. Testes Automatizados

### Por que testar?

Testes automatizados garantem que o código funciona corretamente e continuará funcionando após futuras alterações. Sem testes, qualquer mudança pode quebrar funcionalidades existentes sem que você perceba imediatamente.

### A Pirâmide de Testes

```
     /\
    /  \      Testes E2E (poucos, lentos, caros)
   /----\
  /      \    Testes de Integração
 /--------\
/          \  Testes Unitários (muitos, rápidos, baratos)
```

Este projeto tem dois tipos:

### 1. Testes Unitários — CustomerServiceTest e OrderServiceTest

```java
@ExtendWith(MockitoExtension.class)  // integra JUnit 5 com Mockito
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;  // mock: implementação falsa

    @InjectMocks
    private CustomerServiceImpl customerService;    // instância real com mocks injetados

    @BeforeEach
    void setUp() {
        // executado antes de cada teste
        customer = Customer.builder()...build();
    }

    @Test
    @DisplayName("Should create customer successfully")
    void shouldCreateCustomer() {
        // Arrange: define o comportamento dos mocks
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act: executa o método sendo testado
        CustomerResponse response = customerService.create(createRequest);

        // Assert: verifica o resultado
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("João Silva");
        verify(customerRepository).save(any(Customer.class)); // verifica que save foi chamado
    }
}
```

**JUnit 5:** Framework de testes do Java. `@Test` marca um método como caso de teste. `@BeforeEach` executa antes de cada teste. `@DisplayName` dá um nome legível ao teste.

**Mockito:** Framework de mocking. `@Mock` cria uma implementação falsa do `CustomerRepository` — você controla o que ela retorna. `@InjectMocks` cria o `CustomerServiceImpl` injetando os mocks automaticamente.

**AssertJ:** Biblioteca de asserções fluente. `assertThat(response).isNotNull().hasFieldOrPropertyWithValue(...)` é mais legível que o JUnit clássico `assertEquals(expected, actual)`. `assertThatThrownBy` verifica que uma exceção é lançada.

**Padrão AAA (Arrange-Act-Assert):**
- **Arrange:** Prepara o cenário (mocks, objetos).
- **Act:** Executa o código sendo testado.
- **Assert:** Verifica o resultado esperado.

### 2. Testes de Integração — CustomerControllerIT e OrderControllerIT

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("CustomerController Integration Tests")
class CustomerControllerIT {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("customer_db_test")
            .withUsername("root")
            .withPassword("root");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();  // garante banco limpo antes de cada teste
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequest.builder()...build();

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())          // verifica HTTP 201
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Maria Souza"));
    }
}
```

**@SpringBootTest:** Sobe o contexto Spring completo (todos os Beans) para testar a integração real entre as camadas.

**`webEnvironment = RANDOM_PORT`:** Sobe um servidor HTTP em uma porta aleatória, evitando conflitos se outros serviços estiverem rodando.

**@AutoConfigureMockMvc:** Configura o `MockMvc`, que simula requisições HTTP sem precisar de uma conexão de rede real. Mais rápido que fazer chamadas HTTP reais, mas ainda testa o stack completo do Spring MVC.

**@Testcontainers + MySQLContainer:** Sobe um container Docker com MySQL real durante os testes, via a biblioteca Testcontainers. Isso é superior ao banco H2 em memória porque reproduz exatamente o ambiente de produção — incluindo comportamentos específicos do MySQL.

**@ServiceConnection:** Integração Spring Boot 3 + Testcontainers que configura automaticamente o datasource para apontar para o MySQL do container. Sem isso, você precisaria configurar manualmente a URL JDBC.

**@ActiveProfiles("test"):** Ativa o perfil `test`, carregando o `application-test.yml` que define `ddl-auto: create-drop` (recria o schema a cada execução de testes).

**MockMvc:** Permite executar requisições HTTP simuladas e verificar o resultado. `objectMapper.writeValueAsString(request)` serializa o objeto Java para JSON.

**jsonPath + Hamcrest:** Verifica campos específicos do JSON de resposta. `$.success` acessa o campo `success` na raiz do JSON. `$.data.name` acessa o campo `name` dentro do objeto `data`.

```java
.andExpect(status().isCreated())                        // HTTP 201
.andExpect(status().isOk())                             // HTTP 200
.andExpect(status().isNotFound())                       // HTTP 404
.andExpect(status().isBadRequest())                     // HTTP 400
.andExpect(jsonPath("$.success").value(true))           // campo exato
.andExpect(jsonPath("$.data.name").value("Maria"))      // campo aninhado
.andExpect(jsonPath("$.data", hasSize(2)))              // tamanho da lista
.andExpect(jsonPath("$.data[0].email").exists())        // campo existe
.andExpect(jsonPath("$.errors").isArray())              // é um array
```

A biblioteca **Hamcrest** fornece matchers reutilizáveis (como `hasSize()`, `hasItem()`, `containsString()`) usados dentro do `jsonPath` e também no AssertJ. `import static org.hamcrest.Matchers.*` importa todos os matchers disponíveis.

### Testando o OrderController com Mock de Feign

```java
@MockBean
private CustomerClient customerClient;  // substitui o bean real por um mock no contexto Spring

@Test
void shouldCreateOrder() throws Exception {
    when(customerClient.validateActiveCustomer(anyLong()))
            .thenReturn(ApiResponse.success(activeCustomer));
    // ...
}
```

`@MockBean` é diferente de `@Mock`: ele substitui o Bean no contexto Spring (o Bean real não é criado), enquanto `@Mock` cria um objeto mock fora do contexto Spring. Em testes de integração onde o Spring cria os Beans, use `@MockBean`.

### Alternativas às ferramentas de teste

- **TestNG:** Alternativa ao JUnit, mais popular em alguns contextos legados.
- **REST Assured:** Para testes de integração HTTP com DSL mais fluente que MockMvc. Faz chamadas HTTP reais.
- **WireMock:** Simula serviços HTTP externos (alternativa aos mocks do Feign). Útil para testes de integração onde você não controla o serviço externo.
- **Awaitility:** Para testar sistemas assíncronos (aguarda uma condição ser verdadeira com timeout).

---

## 19. Tipos Java Fundamentais Usados no Projeto

### Optional<T> — Evitando NullPointerException

```java
return customerRepository.findById(id)
        .map(CustomerConverter::toResponse)       // transforma se presente
        .orElseThrow(() -> new CustomerNotFoundException(id));  // lança exceção se ausente
```

`Optional<T>` encapsula um valor que pode ou não existir. Força o desenvolvedor a tratar explicitamente o caso de ausência, evitando `NullPointerException` não tratados. `findById` retorna `Optional<Customer>`:

- `.map(fn)`: Aplica a função se o valor estiver presente.
- `.orElseThrow(fn)`: Retorna o valor ou lança a exceção fornecida.
- `.orElse(valor)`: Retorna o valor ou o padrão fornecido.
- `.isPresent()`: Verifica se o valor existe.

### Stream API — Programação Funcional em Java

```java
return customerRepository.findAll()
        .stream()                              // cria um stream
        .map(CustomerConverter::toResponse)    // transforma cada elemento
        .collect(Collectors.toList());         // coleta o resultado em uma lista
```

A Stream API (Java 8+) permite processar coleções de forma declarativa e funcional:

- `.stream()`: Cria um fluxo de dados.
- `.map(fn)`: Transforma cada elemento.
- `.filter(predicate)`: Filtra elementos que satisfazem a condição.
- `.collect(Collectors.toList())`: Coleta em uma lista.
- `.reduce(identidade, acumulador)`: Reduz a stream a um único valor.

No `OrderConverter`:
```java
BigDecimal total = items.stream()
        .map(OrderItem::getSubtotal)               // pega o subtotal de cada item
        .reduce(BigDecimal.ZERO, BigDecimal::add); // soma todos os subtotais
```

### BigDecimal — Aritmética Monetária Precisa

```java
@Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
private BigDecimal unitPrice;

public BigDecimal getSubtotal() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
}
```

**Nunca use `double` ou `float` para valores monetários!** Esses tipos têm precisão finita em binário e causam erros de arredondamento:

```java
// double (ERRADO para dinheiro)
System.out.println(0.1 + 0.2);  // Imprime: 0.30000000000000004

// BigDecimal (CORRETO)
new BigDecimal("0.1").add(new BigDecimal("0.2"));  // 0.3
```

`precision = 10, scale = 2`: até 10 dígitos no total, com 2 casas decimais (máximo: 99999999.99).

### LocalDateTime — Data e Hora Moderna

```java
private LocalDateTime createdAt;

@PrePersist
void prePersist() {
    createdAt = LocalDateTime.now();
}
```

O Java 8 introduziu a API `java.time` (baseada no Joda-Time) para substituir o problemático `java.util.Date`. `LocalDateTime` representa data e hora sem fuso horário. Para armazenar com fuso horário, use `ZonedDateTime` ou `OffsetDateTime`. O projeto usa `UTC` como fuso horário do banco (`serverTimezone=UTC` na URL JDBC).

### Enum — Tipo de Domínio Seguro

```java
public enum OrderStatus {
    PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}
```

Enums definem um conjunto fechado de valores possíveis. Ao contrário de usar Strings como `"PENDING"`, o enum garante em tempo de compilação que apenas os valores válidos são usados. Se você digitar `OrderStatus.PENDIG`, o compilador avisa antes de o código rodar.

### Generics — Reutilização com Tipo Seguro

```java
public class ApiResponse<T> {
    private T data;  // T pode ser qualquer tipo

    public static <T> ApiResponse<T> success(T data) { ... }
}

// Uso:
ApiResponse<CustomerResponse> response = ApiResponse.success(customerResponse);
ApiResponse<List<OrderResponse>> listResponse = ApiResponse.success(orders);
```

Generics permitem que a mesma classe ou método funcione com diferentes tipos, com verificação em tempo de compilação. `ApiResponse<CustomerResponse>` e `ApiResponse<OrderResponse>` são tipos distintos, mas compartilham toda a lógica da classe `ApiResponse`.

---

## 20. Jackson — Serialização e Desserialização JSON

### O que é o Jackson?

**Jackson** é a biblioteca de serialização JSON padrão do Spring Boot. Toda vez que um objeto Java é convertido para JSON (resposta da API) ou um JSON é convertido para objeto Java (corpo da requisição), é o Jackson que faz esse trabalho — de forma transparente, sem que você precise chamar nada explicitamente.

### Como o Jackson aparece no projeto

**Desserialização (JSON → Java):**
```java
@PostMapping
public ApiResponse<CustomerResponse> create(@RequestBody CreateCustomerRequest request) { ... }
// O Spring chama o Jackson para converter o corpo JSON em CreateCustomerRequest
```

**Serialização (Java → JSON):**
```java
return ApiResponse.success(response, "Customer created successfully");
// O Spring chama o Jackson para converter o ApiResponse em JSON
```

**Nos testes — uso explícito do ObjectMapper:**
```java
@Autowired
private ObjectMapper objectMapper;  // o Bean Jackson configurado pelo Spring Boot

mockMvc.perform(post("/api/v1/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))  // Java → JSON String
```

O `ObjectMapper` é o componente central do Jackson. Nos testes, precisamos chamá-lo explicitamente para gerar o corpo JSON da requisição.

### @JsonInclude — Controlando Campos Nulos

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;  // null em sucesso sem mensagem → não aparece no JSON
    private List<String> errors; // null em sucesso → não aparece no JSON
}
```

| Valor | Comportamento |
|---|---|
| `NON_NULL` | Omite campos com valor `null` |
| `NON_EMPTY` | Omite campos `null`, strings vazias e coleções vazias |
| `ALWAYS` (padrão) | Inclui todos os campos, mesmo `null` |
| `NON_DEFAULT` | Omite campos com valor igual ao padrão do tipo |

Sem `NON_NULL`, um sucesso sem mensagem retornaria `"message": null` no JSON, que é ruído desnecessário para o cliente.

### Configurações Comuns do Jackson

O Spring Boot auto-configura o Jackson com padrões razoáveis. Configurações adicionais podem ser feitas no `application.yml`:

```yaml
spring:
  jackson:
    serialization:
      write-dates-as-timestamps: false  # LocalDateTime como string ISO-8601, não número
    default-property-inclusion: non_null  # equivalente a @JsonInclude global
    property-naming-strategy: SNAKE_CASE  # converte camelCase para snake_case no JSON
```

### Alternativas ao Jackson

- **Gson (Google):** Biblioteca de serialização JSON da Google. Mais simples, menor, mas com menos funcionalidades.
- **Moshi:** Popular em Android e Kotlin, mais leve que Jackson.
- **JSON-B (Jakarta JSON Binding):** Especificação padrão Jakarta EE para serialização JSON.

---

## 21. Postman — Teste Manual de APIs

### O que é o Postman?

Postman é uma ferramenta visual para criar, testar e documentar APIs. Permite fazer requisições HTTP com qualquer método, configurar headers, corpo JSON e verificar as respostas — sem precisar escrever código.

### A Collection do Projeto

O arquivo `imepac-order-system.postman_collection.json` é uma **collection do Postman**: um conjunto organizado de requisições HTTP para testar todos os endpoints do sistema.

Para importar: abra o Postman → Import → selecione o arquivo JSON → todos os endpoints aparecem organizados.

### Por que usar Postman além do Swagger UI?

| Recurso | Swagger UI | Postman |
|---|---|---|
| Explorar a API | Excelente | Bom |
| Salvar requisições | Não | Sim |
| Variáveis de ambiente | Não | Sim |
| Testes automatizados (scripts) | Não | Sim |
| Documentação exportável | Limitado | Sim |
| Compartilhar com equipe | Limitado | Sim |

Com Postman, você pode criar **variáveis de ambiente** (`{{base_url}}`, `{{customer_id}}`) que mudam conforme o ambiente (desenvolvimento, staging, produção) sem recriar as requisições.

### Alternativas ao Postman

- **Insomnia:** Cliente REST mais leve e open source. Similar ao Postman.
- **HTTPie:** Cliente HTTP para o terminal com sintaxe mais amigável que o `curl`.
- **curl:** O cliente HTTP universal do terminal. Disponível em qualquer sistema Unix.
- **IntelliJ HTTP Client:** Arquivos `.http` dentro do próprio IntelliJ IDEA, ideal para versionamento junto ao código.
- **Bruno:** Cliente REST open source e offline-first, com arquivos versionáveis em Git.

---

## 22. Script de Build (build-and-run.sh)

### O que é o script

```bash
#!/bin/bash
set -e  # encerra o script imediatamente se qualquer comando falhar

echo ">>> Installing commons module..."
cd commons && mvn clean install -DskipTests && cd ..

echo ">>> Building customer-service..."
cd customer-service && mvn clean package -DskipTests && cd ..

echo ">>> Building order-service..."
cd order-service && mvn clean package -DskipTests && cd ..

echo ">>> Starting Docker Compose..."
docker-compose up --build -d  # --build: rebuilda as imagens; -d: modo detached (background)
```

O script automatiza a sequência de comandos necessária para construir e subir o sistema completo:

1. **Commons primeiro:** O `commons` precisa estar no repositório local Maven (`.m2`) antes de ser usado pelos outros módulos. `mvn clean install` compila, testa e instala no `.m2` local.
2. **Módulos de serviço:** `mvn clean package` compila e gera o JAR sem instalar no `.m2`.
3. **Docker Compose:** Constrói as imagens Docker (usando os JARs gerados) e sobe os containers.

`set -e` garante que, se o build do `commons` falhar, o script para imediatamente em vez de tentar continuar com um artefato desatualizado.

### Por que isso é importante?

A ordem de build importa porque o `customer-service` e o `order-service` dependem do `commons`. Se você tentar construí-los antes de instalar o `commons`, o Maven não encontrará o artefato no repositório local e o build falhará.

Em projetos profissionais, esse script seria substituído por um **pipeline de CI/CD** (GitHub Actions, GitLab CI, Jenkins, CircleCI), que automatiza o build, testes e deploy em cada push para o repositório.

---

## Mapa de Tecnologias por Arquivo

| Arquivo / Localização | Tecnologias Abordadas |
|---|---|
| `pom.xml` (raiz) | Maven Multi-module, BOM, dependencyManagement, plugins |
| `*/pom.xml` | Maven modules, scopes (runtime, test, optional) |
| `*Application.java` | Spring Boot, @SpringBootApplication, IoC Container |
| `*Controller.java` | Spring MVC, REST, @RestController, HTTP verbs, @PathVariable, @RequestParam |
| `*Entity.java` | JPA, @Entity, @Column, @OneToMany, @ManyToOne, @PrePersist, Lombok |
| `*Repository.java` | Spring Data JPA, JpaRepository, Query Derivation, @Query JPQL |
| `*Service.java` | Interface Segregation (SOLID), Design Pattern Service |
| `*ServiceImpl.java` | @Transactional, Stream API, Optional, Lombok @Slf4j |
| `*Request.java` / `*Response.java` | DTO Pattern, Bean Validation, Lombok @Builder |
| `*Converter.java` | Converter Pattern, Builder Pattern, estáticos utilitários |
| `GlobalExceptionHandler.java` | @RestControllerAdvice, @ExceptionHandler, HTTP Status Codes |
| `ApiResponse.java` | Generics, @JsonInclude, Builder Pattern |
| `CustomerClient.java` | OpenFeign, @FeignClient, comunicação inter-serviços |
| `FeignConfig.java` | Feign ErrorDecoder, switch expression (Java 14+) |
| `SwaggerConfig.java` | OpenAPI 3, @Configuration, @Bean |
| `CommonsAutoConfiguration.java` | Spring Auto-configuration, AutoConfiguration.imports, SPI |
| `OrderStatus.java` | Java Enum, @Enumerated |
| `application.yml` | YAML, variáveis de ambiente, Spring profiles, JPA config |
| `application-test.yml` | Spring profiles para testes |
| `*IT.java` | Testes de Integração, @SpringBootTest, MockMvc, Testcontainers |
| `*Test.java` | Testes Unitários, JUnit 5, Mockito, AssertJ |
| `Dockerfile` | Docker, imagem base alpine, ENTRYPOINT, ARG |
| `docker-compose.yml` | Docker Compose, healthcheck, depends_on, networks, volumes |
| `build-and-run.sh` | Shell Script, Maven lifecycle, Docker Compose CLI |

---

## Fluxo Completo de uma Requisição

Para fixar todos os conceitos, veja o que acontece quando o frontend envia `POST /api/v1/orders` para criar um pedido:

```
1. HTTP Request chega ao Tomcat (embutido pelo Spring Boot)
2. Spring MVC roteia para OrderController.create()
3. @RequestBody deserializa o JSON → CreateOrderRequest (Jackson/ObjectMapper)
4. @Valid aciona Bean Validation → se inválido, lança MethodArgumentNotValidException
   → GlobalExceptionHandler.handleValidation() → HTTP 400 Bad Request
5. OrderController chama orderService.create(request)
6. @Transactional abre uma transação JPA
7. customerClient.validateActiveCustomer(customerId) — chamada HTTP via Feign
   → FeignConfig.ErrorDecoder processa erros HTTP do customer-service
   → se cliente inativo: FeignIntegrationException → GlobalExceptionHandler → HTTP 502
8. OrderConverter.toEntity(request) — converte o DTO em entidade JPA
9. orderRepository.save(order) — Hibernate gera INSERT INTO orders e INSERT INTO order_items
10. @PrePersist preenche createdAt e status padrão
11. @Transactional faz commit
12. OrderConverter.toResponse(saved) — converte entidade em DTO de resposta
13. ApiResponse.success(response, "Order created successfully") — envelope padronizado
14. @ResponseStatus(HttpStatus.CREATED) define o código HTTP 201
15. @ResponseBody serializa ApiResponse para JSON (Jackson)
16. Logbook registra a requisição e resposta nos logs
17. HTTP 201 Created com o JSON retorna ao cliente
```

---

## Glossário Rápido

| Termo | Definição |
|---|---|
| **API** | Application Programming Interface — interface para comunicação entre sistemas |
| **REST** | Estilo arquitetural para APIs sobre HTTP |
| **JSON** | JavaScript Object Notation — formato de troca de dados |
| **ORM** | Object-Relational Mapping — mapeamento entre objetos Java e tabelas do banco |
| **JPA** | Jakarta Persistence API — especificação de ORM para Java |
| **DTO** | Data Transfer Object — objeto para transferir dados entre camadas |
| **IoC** | Inversion of Control — o framework controla a criação e ciclo de vida dos objetos |
| **DI** | Dependency Injection — forma de implementar IoC injetando dependências |
| **Bean** | Objeto gerenciado pelo container Spring |
| **JPQL** | Java Persistence Query Language — linguagem de consulta sobre entidades JPA |
| **Microsserviço** | Serviço pequeno e independente com responsabilidade única |
| **Container** | Processo isolado com seu próprio ambiente de execução (Docker) |
| **Fat JAR** | JAR executável com todas as dependências embutidas |
| **Mock** | Implementação falsa de uma dependência usada em testes |
| **Boilerplate** | Código repetitivo e mecânico sem lógica de negócio |
| **CI/CD** | Continuous Integration / Continuous Deployment — automação de build e deploy |
| **SOLID** | Cinco princípios de design orientado a objetos (S, O, L, I, D) |
| **BOM** | Bill of Materials — catálogo de versões compatíveis de dependências |
| **N+1** | Problema de performance: 1 query para a lista + N queries para os filhos |
