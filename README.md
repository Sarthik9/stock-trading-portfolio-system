# 📈 Stock Trading Portfolio System

A backend application built with **Java and Spring Boot** that simulates core stock-trading operations such as BUY/SELL order execution, portfolio management, wallet management, transaction tracking, and secure user authentication.

The project focuses on **transactional business logic, database consistency, caching, API security, rate limiting, and idempotent order processing**.

---

## 🚀 Key Features

* BUY and SELL stock orders
* Wallet management with debit/credit operations
* Portfolio tracking with average stock price
* Transaction history
* JWT-based authentication and authorization
* Redis caching with TTL and cache eviction
* Redis-based rate limiting for order APIs
* Idempotent order creation using `Idempotency-Key`
* Database-level uniqueness protection for duplicate orders
* Transactional order processing
* Global exception handling
* Input validation
* Dockerized MySQL database
* RESTful APIs

---

## 🛠 Tech Stack

| Layer             | Technology                  |
| ----------------- | --------------------------- |
| Language          | Java 17                     |
| Framework         | Spring Boot                 |
| ORM               | Spring Data JPA / Hibernate |
| Database          | MySQL                       |
| Cache             | Redis / Memurai             |
| Security          | Spring Security + JWT       |
| Build Tool        | Maven                       |
| Containerization  | Docker / Docker Compose     |
| API Documentation | Swagger / OpenAPI           |
| Testing           | JUnit / Mockito             |
| Version Control   | Git & GitHub                |

---

## 🧱 Architecture

The application follows a layered architecture:

```text
Client
   ↓
Controller
   ↓
Security / Filters
   ↓
Service Layer
   ↓
Repository Layer
   ↓
MySQL
```

Redis is used alongside the application for caching, rate limiting, and idempotency.

### Main Layers

* **Controller** → Handles HTTP requests and responses
* **Service** → Contains business and trading logic
* **Repository** → Handles database access through JPA
* **Entity** → Represents database tables
* **Security** → JWT authentication and authorization
* **Redis** → Caching, rate limiting, and idempotency

---

# 🔥 Core Modules

## 1️⃣ Orders

Handles BUY and SELL operations.

### Features

* Create BUY orders
* Create SELL orders
* Retrieve orders
* Idempotent order creation
* Database uniqueness protection
* Transactional order execution

### Example

```http
POST /orders/createOrder
Idempotency-Key: 7f3a8c21
```

The same idempotency key for the same user represents the same logical order request.

A database constraint ensures:

```text
(uid, idempotency_key) → UNIQUE
```

This provides a final database-level safeguard against duplicate orders.

---

## 2️⃣ Portfolio

Maintains the user's current stock holdings.

### Features

* Track stock quantity
* Maintain average purchase price
* Update holdings after BUY/SELL
* Redis caching
* Cache eviction after portfolio updates

Example:

```text
TCS  → 10 shares
INFY → 5 shares
```

---

## 3️⃣ Wallet

Manages user funds.

### Features

* Add money
* Check balance
* Debit funds during BUY
* Credit funds during SELL

The wallet operations participate in the same transaction as the order, portfolio, and transaction updates.

---

## 4️⃣ Transactions

Maintains executed trading history.

### Features

* BUY transaction records
* SELL transaction records
* Quantity and price tracking
* Timestamped transaction history

---

## 5️⃣ Authentication & Authorization

Implemented using:

* Spring Security
* JWT
* BCrypt password hashing
* Role-based authorization

Example roles:

```text
USER
ADMIN
```

JWT authentication is applied through a custom security filter.

---

# 🔄 Order Processing Flow

## BUY

```text
Client
  ↓
JWT Authentication
  ↓
Rate Limiting
  ↓
Idempotency Check
  ↓
Validate Wallet Balance
  ↓
Debit Wallet
  ↓
Update Portfolio
  ↓
Create Transaction
  ↓
Save Order
  ↓
Cache Idempotency Response
```

## SELL

```text
Client
  ↓
JWT Authentication
  ↓
Rate Limiting
  ↓
Idempotency Check
  ↓
Validate Stock Holdings
  ↓
Update Portfolio
  ↓
Credit Wallet
  ↓
Create Transaction
  ↓
Save Order
  ↓
Cache Idempotency Response
```

Order processing is designed to execute the database operations transactionally.

---

# ⚡ Redis

Redis is used for multiple backend concerns.

### 1. Caching

Portfolio data is cached to reduce repeated database reads.

```text
Request
   ↓
Redis Cache
   ↓
Cache Hit → Return data

Cache Miss
   ↓
MySQL
   ↓
Store in Redis
```

Configured with TTL-based expiration.

### 2. Cache Eviction

Portfolio cache is evicted when holdings are modified to prevent stale data.

### 3. Rate Limiting

Order creation is protected with Redis-based rate limiting.

Example:

```text
Maximum requests = 60
Window = 1 minute
```

The rate limit is applied at the filter level before the request reaches the business logic.

### 4. Idempotency

Order creation uses an `Idempotency-Key`.

Redis stores the response for a limited period so repeated requests can return the original result instead of creating another order.

Redis failure is handled using a **fail-open approach** where possible, allowing the database-backed order flow to continue.

---

# 🛡️ Idempotency Design

The order table contains:

```text
uid
idempotency_key
```

with a unique database constraint:

```sql
UNIQUE (uid, idempotency_key)
```

This provides two levels of protection:

```text
Redis
  ↓
Fast duplicate detection

MySQL
  ↓
Final uniqueness guarantee
```

Idempotency keys are supplied through the request header:

```http
Idempotency-Key: <unique-key>
```

---

# 🔐 Security

The application uses Spring Security with JWT authentication.

Security flow:

```text
Request
   ↓
JWT Filter
   ↓
Validate Token
   ↓
Set Authentication
   ↓
Authorization
   ↓
Controller
```

Additional security features include:

* Stateless authentication
* BCrypt password hashing
* Role-based access control
* Protected APIs
* Authentication and authorization exception handling

---

# 🚦 Rate Limiting

Redis is used to limit order creation requests.

```text
POST /orders/createOrder
        ↓
RateLimitFilter
        ↓
Redis Counter
        ↓
Allowed → Continue
Rejected → HTTP 429
```

This prevents excessive requests from reaching the order-processing logic.

---

# 🗄️ Database Design

### `stock_orders`

Stores executed orders.

```text
order_id
uid
stock
price
quantity
order_type
idempotency_key
```

### `portfolio`

Stores current holdings.

```text
user_id
stock
quantity
avg_price
```

### `wallet`

Stores user balances.

```text
user_id
balance
```

### `transactions`

Stores trading history.

```text
id
user_id
stock
quantity
price
type
timestamp
```

---

# 🐳 Docker

MySQL runs using Docker Compose.

```bash
docker compose up -d
```

The application can connect to the containerized MySQL database while Redis runs locally through Memurai during development.

---

# ⚙️ Setup

## 1. Clone

```bash
git clone https://github.com/Sarthik9/stock-trading-portfolio-system.git

cd stock-trading-portfolio-system
```

## 2. Start MySQL

```bash
docker compose up -d
```

## 3. Start Redis

Start the local Memurai Redis server.

Verify:

```bash
memurai-cli.exe ping
```

Expected:

```text
PONG
```

## 4. Run the application

```bash
mvn spring-boot:run
```

---

# 📚 Key Backend Concepts Demonstrated

* REST API design
* Layered architecture
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Database transactions
* Transactional consistency
* Redis caching
* Cache eviction
* Cache TTL
* Redis rate limiting
* Idempotency
* Database unique constraints
* Spring Security
* JWT authentication
* Role-based authorization
* BCrypt
* Exception handling
* Input validation
* Docker
* API documentation
* Unit testing

---

# 🧠 Engineering Concepts

This project focuses on several real-world backend problems:

### Data Consistency

Wallet, portfolio, transaction, and order updates are handled as part of the order-processing transaction.

### Duplicate Requests

Idempotency prevents client retries from creating multiple logical orders.

### High-Frequency Requests

Redis-based rate limiting protects the order API.

### Database Load

Redis caching reduces repeated reads for frequently accessed portfolio data.

### Dependency Failure

Redis failures are handled without unnecessarily blocking the core database-backed order flow.

---

# 🚧 Planned Enhancements

* Kafka-based event-driven architecture
* Asynchronous order/transaction events
* Distributed locking where required
* Advanced Redis atomic operations using Lua
* Production-grade idempotency state management
* AWS deployment
* Monitoring and observability
* Real-time market price integration
* Microservices decomposition

---

# 📈 Future Architecture

The planned architecture will introduce Kafka for asynchronous event processing:

```text
                 ┌─────────────┐
                 │   Client    │
                 └──────┬──────┘
                        ↓
                 ┌─────────────┐
                 │ Spring Boot │
                 └──────┬──────┘
                        ↓
                 ┌─────────────┐
                 │   MySQL     │
                 └─────────────┘
                        │
                        ↓
                 ┌─────────────┐
                 │    Kafka    │
                 └──────┬──────┘
                        ↓
             ┌──────────┴──────────┐
             ↓                     ↓
      Transaction Service    Notification Service
```

---

# 👨‍💻 Author

**Sarthi**

---

## ⭐ Project Goal

The project is continuously evolving toward a production-oriented backend system while focusing on practical implementation of **Java, Spring Boot, databases, Redis, security, distributed-system concepts, and event-driven architecture**.
