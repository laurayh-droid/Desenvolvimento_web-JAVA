# Módulo de Orders — order-service

Este documento serve como apoio para criação das **tabelas MySQL** utilizadas pelo `order-service` (JPA/Hibernate).

> As tabelas abaixo foram derivadas dos `@Entity`, `@Table(name=...)`, `@Column(name=...)` e relacionamentos do código.

---

## 1) Tabelas utilizadas

- `orders`
- `order_items`

---

## 2) Tabela `orders`

**Campos (mapeamento dos atributos da entidade `Order`)**:

- `id` (PK, auto increment)
- `customer_id` (bigint, NOT NULL)
- `status` (varchar(50), NOT NULL) — enum `OrderStatus`
- `total_amount` (decimal(10,2), NOT NULL)
- `created_at` (datetime, NOT NULL, updatable=false)
- `updated_at` (datetime, NULL)


---

## 3) Tabela `order_items`

**Campos (mapeamento dos atributos da entidade `OrderItem`)**:

- `id` (PK, auto increment)
- `order_id` (bigint, NOT NULL) — FK para `orders.id`
- `product_name` (varchar(200), NOT NULL)
- `quantity` (int, NOT NULL)
- `unit_price` (decimal(10,2), NOT NULL)

---

## 4) SQL exemplo (base)

```sql
CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL
);

CREATE TABLE order_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  product_name VARCHAR(200) NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,

  CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
```

---

## 5) Observações

- `order-service` usa relacionamento `@OneToMany` entre `Order` e `OrderItem`.
- Se o seu `application.yml` estiver com `spring.jpa.hibernate.ddl-auto=update` (ou `create`), o Hibernate pode criar/ajustar o schema automaticamente.
- Ajuste tamanho/charset/nullable conforme seu padrão do projeto.

