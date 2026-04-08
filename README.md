# 📈 Stock Trading Portfolio System

## 🚀 Overview

The **Stock Trading Portfolio System** is a backend application that simulates real-world stock trading operations.
It allows users to place buy/sell orders, manage their portfolio, track transactions, and maintain wallet balances.

This project is designed to demonstrate **backend engineering skills**, including system design, database management, and scalable architecture using Spring Boot.

---

## 🎯 Objectives

* Build a scalable backend system for stock trading
* Implement real-world concepts like order execution, portfolio tracking, and wallet management
* Follow clean architecture and industry best practices
* Prepare for backend engineering interviews

---

## 🛠 Tech Stack

| Layer           | Technology                  |
| --------------- | --------------------------- |
| Language        | Java 17                     |
| Framework       | Spring Boot                 |
| ORM             | Spring Data JPA (Hibernate) |
| Database        | H2 (in-memory)              |
| Build Tool      | Maven                       |
| Version Control | Git & GitHub                |

---

## 🧱 System Architecture

The system is designed with separation of concerns:

* **Controller Layer** → Handles API requests
* **Service Layer** → Contains business logic
* **Repository Layer** → Interacts with database
* **Entity Layer** → Represents database tables

---

## 🔥 Core Modules

### 1️⃣ Orders Module

Handles user trading requests.

#### Features:

* Create BUY/SELL orders
* Fetch all orders
* Fetch order by ID

#### APIs:

```
POST /orders
GET /orders
GET /orders/{id}
```

---

### 2️⃣ Portfolio Module

Represents the **current holdings of a user**.

#### Features:

* Aggregates stock quantities
* Maintains average price
* Reflects real-time holdings

#### Example:

```
TCS → 10 shares
INFY → 5 shares
```

#### API:

```
GET /portfolio/{userId}
```

---

### 3️⃣ Wallet Module

Manages user funds.

#### Features:

* Add money to wallet
* Deduct balance on BUY
* Credit balance on SELL

#### APIs:

```
GET /wallet/{userId}
POST /wallet/add
```

---

### 4️⃣ Transactions Module

Stores executed trade history.

#### Features:

* Records BUY/SELL execution
* Maintains timestamped history

#### API:

```
GET /transactions/{userId}
```

---

## 🔄 System Flow

### BUY Operation

1. Validate wallet balance
2. Deduct amount from wallet
3. Update portfolio holdings
4. Record transaction
5. Save order

---

### SELL Operation

1. Validate portfolio quantity
2. Reduce stock holdings
3. Add money to wallet
4. Record transaction
5. Save order

---

## 🗄 Database Design

### Order Entity

* id (UUID)
* stock
* price
* quantity
* orderType (BUY/SELL)

### Portfolio Entity

* userId
* stock
* quantity
* avgPrice

### Wallet Entity

* userId
* balance

### Transaction Entity

* id (UUID)
* userId
* stock
* quantity
* price
* type
* timestamp

---

## ⚙️ Setup & Run

### 1️⃣ Clone Repository

```
git clone https://github.com/Sarthik9/stock-trading-portfolio-system.git
cd stock-trading-portfolio-system
```

### 2️⃣ Run Application

```
mvn spring-boot:run
```

---

## 🧪 H2 Database Access

Open:

```
http://localhost:8080/h2-console
```

Use:

```
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (empty)
```

---

## 📌 Key Concepts Used

* REST API design
* Layered architecture
* JPA & ORM mapping
* UUID-based primary keys
* Enum handling in database
* In-memory database usage

---

## 🚧 Future Enhancements

* 🔄 Microservices architecture
* 🔄 Kafka for event-driven processing
* 🔄 Redis caching
* 🔄 Authentication & Authorization (JWT)
* 🔄 Deployment on AWS
* 🔄 Real-time stock price integration
* 🔄 Docker containerization

---

## 🧠 Learning Outcomes

* Designed a real-world backend system
* Implemented business logic for trading flow
* Learned database integration with JPA
* Improved system design thinking

---

## 👨‍💻 Author

**Sarthik Mehra**

---

## ⭐ Contribution

Feel free to fork this repository and enhance it further!
