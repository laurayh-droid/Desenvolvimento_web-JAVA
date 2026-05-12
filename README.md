# Order System — Microsserviços com Spring Boot

Sistema de gerenciamento de clientes e pedidos construído com arquitetura de microsserviços. Desenvolvido com Spring Boot 3.2, Java 17 e MySQL.

---

## Arquitetura

```
┌─────────────────────────────────────────────────────────┐
│                      Cliente / Postman                   │
└───────────────┬─────────────────────┬───────────────────┘
                │                     │
                ▼                     ▼
   ┌────────────────────┐  ┌────────────────────┐
   │  customer-service  │  │   order-service    │
   │    porta 8081      │  │    porta 8082      │
   └────────┬───────────┘  └────────┬───────────┘
            │                       │
            │          Feign (HTTP) │
            │◄──────────────────────┘
            │     GET /api/v1/customers/{id}/validate-active
            │
   ┌────────▼───────────┐  ┌────────────────────┐
   │   MySQL            │  │   MySQL            │
   │   customer_db      │  │   order_db         │
   │   porta 3307       │  │   porta 3308       │
   └────────────────────┘  └────────────────────┘
```

Antes de criar um pedido, o **order-service** consulta o **customer-service** via Feign para garantir que o cliente existe e está ativo.

---

## Módulos

| Módulo | Porta | Responsabilidade |
|---|---|---|
| `commons` | — | DTOs compartilhados, `ApiResponse`, `GlobalExceptionHandler`, enums, exceptions base |
| `customer-service` | **8081** | CRUD completo de clientes, validação de cliente ativo |
| `order-service` | **8082** | CRUD de pedidos, integração com customer-service via Feign |

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java | 17 |
| Maven | 3.8 |
| Docker + Docker Compose | 20.x / 2.x |

> Para rodar localmente sem Docker é necessário ter MySQL 8.0 disponível.

---

## Como Rodar

### Opção A — Docker Compose (recomendado)

Sobe todos os serviços e bancos de dados sem nenhuma configuração adicional.

```bash
# 1. Gerar os JARs
mvn clean package -DskipTests

# 2. Subir toda a stack
docker-compose up --build
```

Serviços iniciados:

| Container | Porta externa | Descrição |
|---|---|---|
| `mysql-customer` | 3307 | MySQL do customer-service |
| `mysql-order` | 3308 | MySQL do order-service |
| `customer-service` | 8081 | API de clientes |
| `order-service` | 8082 | API de pedidos |

```bash
# Derrubar e remover volumes
docker-compose down -v
```

---

### Opção B — Execução local (IDE ou terminal)

Requer MySQL rodando em `localhost:3306` com usuário `root` e senha `root` (ou configurar via variáveis de ambiente).

```bash
# 1. Compilar e instalar o módulo commons
mvn clean install -pl commons

# 2. Subir o customer-service (terminal 1)
mvn spring-boot:run -pl customer-service

# 3. Subir o order-service (terminal 2)
CUSTOMER_SERVICE_URL=http://localhost:8081 mvn spring-boot:run -pl order-service
```

#### Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_HOST` | `localhost` | Host do MySQL |
| `DB_PORT` | `3306` | Porta do MySQL |
| `DB_NAME` | `customer_db` / `order_db` | Nome do banco (por serviço) |
| `DB_USER` | `root` | Usuário do MySQL |
| `DB_PASSWORD` | `root` | Senha do MySQL |
| `CUSTOMER_SERVICE_URL` | `http://localhost:8081` | URL base do customer-service (somente order-service) |

---

## Documentação Interativa (Swagger)

Com os serviços rodando, acesse:

- **customer-service:** http://localhost:8081/swagger-ui.html
- **order-service:** http://localhost:8082/swagger-ui.html

---

## Endpoints

### Customer Service — `http://localhost:8081`

| Método | Rota | Descrição | Status |
|---|---|---|---|
| `POST` | `/api/v1/customers` | Criar cliente | 201 |
| `GET` | `/api/v1/customers` | Listar todos os clientes | 200 |
| `GET` | `/api/v1/customers?activeOnly=true` | Listar apenas clientes ativos | 200 |
| `GET` | `/api/v1/customers/{id}` | Buscar cliente por ID | 200 |
| `PUT` | `/api/v1/customers/{id}` | Atualizar cliente | 200 |
| `DELETE` | `/api/v1/customers/{id}` | Remover cliente | 204 |
| `GET` | `/api/v1/customers/{id}/validate-active` | Validar se cliente está ativo (uso interno) | 200 |

### Order Service — `http://localhost:8082`

| Método | Rota | Descrição | Status |
|---|---|---|---|
| `POST` | `/api/v1/orders` | Criar pedido | 201 |
| `GET` | `/api/v1/orders` | Listar todos os pedidos | 200 |
| `GET` | `/api/v1/orders/{id}` | Buscar pedido por ID | 200 |
| `GET` | `/api/v1/orders/customer/{customerId}` | Listar pedidos de um cliente | 200 |
| `PATCH` | `/api/v1/orders/{id}/status` | Atualizar status do pedido | 200 |

---

## Regras de Negócio

- O **e-mail** do cliente deve ser único no sistema.
- Um pedido só pode ser criado para um **cliente ativo**. Caso o cliente não exista ou esteja inativo, a criação retorna `502 Bad Gateway`.
- O **status** do pedido segue o fluxo abaixo:

```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
                                           ↘
                                         CANCELLED
```

- `DELIVERED` e `CANCELLED` são **estados terminais** — nenhuma transição é permitida a partir deles.
- Não é possível **regredir** para `PENDING` após qualquer outra transição.

---

## Exemplos de Uso — Fluxo Completo

Os exemplos abaixo usam `curl`. Execute-os em ordem para percorrer o fluxo completo.

### 1. Criar um cliente

```bash
curl -s -X POST http://localhost:8081/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "phone": "(11) 91234-5678"
  }' | python3 -m json.tool
```

Resposta esperada (`201 Created`):
```json
{
  "success": true,
  "message": "Customer created successfully",
  "data": {
    "id": 1,
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "phone": "(11) 91234-5678",
    "active": true,
    "createdAt": "2026-05-05T10:00:00"
  }
}
```

> Anote o `id` retornado — será usado nas próximas chamadas como `{customerId}`.

---

### 2. Listar todos os clientes

```bash
curl -s http://localhost:8081/api/v1/customers | python3 -m json.tool
```

---

### 3. Buscar cliente por ID

```bash
# Substitua 1 pelo ID retornado na criação
curl -s http://localhost:8081/api/v1/customers/1 | python3 -m json.tool
```

---

### 4. Atualizar cliente

```bash
curl -s -X PUT http://localhost:8081/api/v1/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Atualizado",
    "phone": "(11) 99999-8888"
  }' | python3 -m json.tool
```

---

### 5. Criar um pedido

```bash
# Substitua "customerId": 1 pelo ID do cliente criado
curl -s -X POST http://localhost:8082/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {
        "productName": "Notebook Dell Inspiron 15",
        "quantity": 1,
        "unitPrice": 3499.90
      },
      {
        "productName": "Mouse Logitech MX Master 3",
        "quantity": 2,
        "unitPrice": 349.99
      }
    ]
  }' | python3 -m json.tool
```

Resposta esperada (`201 Created`):
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "customerId": 1,
    "status": "PENDING",
    "totalAmount": 4199.88,
    "items": [...]
  }
}
```

> Anote o `id` do pedido — será usado como `{orderId}` nos próximos exemplos.

---

### 6. Listar pedidos de um cliente

```bash
curl -s http://localhost:8082/api/v1/orders/customer/1 | python3 -m json.tool
```

---

### 7. Avançar o status do pedido

```bash
# PENDING → CONFIRMED
curl -s -X PATCH http://localhost:8082/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMED"}' | python3 -m json.tool

# CONFIRMED → PROCESSING
curl -s -X PATCH http://localhost:8082/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "PROCESSING"}' | python3 -m json.tool

# PROCESSING → SHIPPED
curl -s -X PATCH http://localhost:8082/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "SHIPPED"}' | python3 -m json.tool

# SHIPPED → DELIVERED
curl -s -X PATCH http://localhost:8082/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "DELIVERED"}' | python3 -m json.tool
```

---

### 8. Cenários de erro

**Tentar criar pedido para cliente inexistente (`502 Bad Gateway`):**
```bash
curl -s -X POST http://localhost:8082/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 99999,
    "items": [{"productName": "Produto", "quantity": 1, "unitPrice": 10.00}]
  }' | python3 -m json.tool
```

**Tentar criar pedido com dados inválidos (`400 Bad Request`):**
```bash
curl -s -X POST http://localhost:8082/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [{"productName": "", "quantity": 0, "unitPrice": 0.00}]
  }' | python3 -m json.tool
```

**Tentar criar cliente com e-mail duplicado (`422 Unprocessable Entity`):**
```bash
curl -s -X POST http://localhost:8081/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Outro João",
    "email": "joao.silva@email.com"
  }' | python3 -m json.tool
```

**Tentar avançar status de pedido já entregue (`422 Unprocessable Entity`):**
```bash
curl -s -X PATCH http://localhost:8082/api/v1/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "CONFIRMED"}' | python3 -m json.tool
```

---

## Testes Automatizados

Os testes de integração utilizam **Testcontainers** — é necessário ter o Docker rodando.

```bash
# Rodar testes do customer-service
mvn test -pl customer-service

# Rodar testes do order-service
mvn test -pl order-service

# Rodar todos os testes
mvn test
```

| Módulo | Tipo | Cobertura |
|---|---|---|
| `customer-service` | Integração (Controller) + Unidade (Service) | CRUD completo, validações, erros 400/404/422 |
| `order-service` | Integração (Controller) + Unidade (Service) | Criação, listagem, transições de status, erros 400/404/502 |

---

## Estrutura do Projeto

```
app-order-service/
├── commons/                          # Módulo compartilhado
│   └── src/main/java/com/imepac/commons/
│       ├── config/                   # CommonsAutoConfiguration
│       ├── dto/                      # CustomerDTO, OrderDTO, OrderItemDTO
│       ├── enums/                    # OrderStatus
│       ├── exception/                # BusinessException, EntityNotFoundException, FeignIntegrationException
│       ├── handler/                  # GlobalExceptionHandler
│       ├── response/                 # ApiResponse<T>
│       └── util/                     # DateUtils
│
├── customer-service/                 # Microsserviço de clientes
│   └── src/main/java/com/imepac/customer/
│       ├── controller/               # CustomerController
│       ├── converter/                # CustomerConverter
│       ├── dto/                      # CreateCustomerRequest, UpdateCustomerRequest, CustomerResponse
│       ├── entity/                   # Customer
│       ├── exception/                # CustomerNotFoundException
│       ├── repository/               # CustomerRepository
│       └── service/                  # CustomerService + CustomerServiceImpl
│
├── order-service/                    # Microsserviço de pedidos
│   └── src/main/java/com/imepac/order/
│       ├── client/                   # CustomerClient (Feign)
│       ├── config/                   # FeignConfig, SwaggerConfig
│       ├── controller/               # OrderController
│       ├── converter/                # OrderConverter
│       ├── dto/                      # CreateOrderRequest, UpdateOrderStatusRequest, OrderResponse, ...
│       ├── entity/                   # Order, OrderItem
│       ├── exception/                # OrderNotFoundException
│       ├── repository/               # OrderRepository
│       └── service/                  # OrderService + OrderServiceImpl
│
├── docker-compose.yml
├── pom.xml                           # POM pai (Java 17, Spring Boot 3.2, Spring Cloud 2023)
└── README.md
```

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem |
| Spring Boot | 3.2.0 | Framework principal |
| Spring Cloud OpenFeign | 2023.0.0 | Comunicação entre serviços |
| Spring Data JPA / Hibernate | — | ORM |
| MySQL | 8.0 | Banco de dados |
| SpringDoc OpenAPI | 2.3.0 | Documentação Swagger |
| Logbook | 3.8.0 | Logging de requisições HTTP |
| Testcontainers | — | Testes de integração com MySQL real |
| Lombok | — | Redução de boilerplate |
| Docker / Docker Compose | — | Containerização |
