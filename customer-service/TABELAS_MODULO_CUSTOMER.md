# Módulo de Customer — customer-service

Este documento serve como apoio para criação das **tabelas MySQL** utilizadas pelo `customer-service` (JPA/Hibernate).

> As tabelas abaixo foram derivadas dos `@Entity`, `@Table(name=...)` e `@Column(name=...)` do código.

---

## 1) Tabelas utilizadas

- `customers`

---

## 2) Tabela `customers`

**Campos (mapeamento da entidade `Customer`)**:

- `id` (PK, auto increment)
- `name` (varchar(150), NOT NULL)
- `email` (varchar(150), NOT NULL, UNIQUE)
- `phone` (varchar(20), NULL)
- `active` (boolean, NOT NULL)
- `created_at` (datetime, NOT NULL, updatable=false)
- `updated_at` (datetime, NULL)

---

## 3) SQL exemplo (base)

```sql
CREATE TABLE customers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  phone VARCHAR(20) NULL,
  active BOOLEAN NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NULL
);
```

---

## 4) Observações

- Se o seu `application.yml` estiver com `spring.jpa.hibernate.ddl-auto=update` (ou `create`), o Hibernate pode criar/ajustar o schema automaticamente.
- Ajuste tamanho/charset/nullable conforme seu padrão do projeto.

