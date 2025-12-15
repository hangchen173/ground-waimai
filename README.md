# 🍽️ Restaurant Reservation System

**Java Final Project – Spring Boot 3.24**

A full-featured **Restaurant Reservation REST API** designed as the final exam project for the Java Programming (OOP) course taught by Prof. Baptiste Dupuis.

This document explains the **architecture**, **design decisions**, **API endpoints**, **data model**, **business rules**, **error handling**, and **testing strategy**.
It serves as both **technical documentation** and **project justification**.

---

# 📖 Table of Contents

1. [Project Overview](#project-overview)
2. [Core Features](#core-features)
3. [Architecture & Design](#architecture--design)
4. [Domain Model](#domain-model)
5. [Business Rules](#business-rules)
6. [API Endpoints](#api-endpoints)
7. [Validation & Exception Handling](#validation--exception-handling)
8. [Tests](#tests)
9.  [Tech Stack](#tech-stack)
10. [How to Run](#how-to-run)
11. [Future Improvements / Bonus](#future-improvements--bonus)
12. [Conclusion](#conclusion)

---

# 📌 Project Overview

The **Restaurant Reservation System** is a backend API built using **Spring Boot 3 + Java 21**, designed to manage:

* Restaurants
* Tables
* Customers
* Reservation scheduling
* Availability checking
* Business rule validation

This API could power real-world systems like OpenTable or TheFork.
The design follows **Clean Architecture**, **SOLID principles**, and **RESTful conventions**.

---

# ⭐ Core Features

### 1. Restaurant Management

* List all restaurants
* Get restaurant details including tables
* Check availability for a specific date

### 2. Reservation Management

* Create a reservation
* Cancel reservation
* View reservation detail
* Show customer's reservation history

### 3. Customer Management

* Register customer
* Get customer profile

---

# 🧠 Architecture & Design

## Layered Architecture

```
controller → service → repository → database
          ↑ DTO/Mapper ↓
```

### ✔ Controllers

Expose REST API endpoints, handle request/response mapping.

### ✔ Services

Contain business logic (availability checking, opening hours, capacity validation, status transitions).

### ✔ Repositories

Spring Data JPA for database CRUD operations.

### ✔ Entities

Persisted domain objects.

### ✔ DTOs

Used to communicate with the outside world (never expose entities).

### ✔ Exceptions

Custom exceptions for business rule violations.

---

# 🏛 Domain Model

## Entities Summary

### 1. **Restaurant**

| Field       | Description                        |
| ----------- | ---------------------------------- |
| id          | Primary key                        |
| name        | Restaurant name                    |
| openingTime | Opening hour (e.g., 11:00)         |
| closingTime | Closing hour (e.g., 22:00)         |
| List<Table> | Tables belonging to the restaurant |

---

### 2. **Table**

| Field      | Description                  |
| ---------- | ---------------------------- |
| id         | Primary key                  |
| capacity   | Number of seats              |
| restaurant | Many tables → One restaurant |

---

### 3. **Customer**

| Field        | Description          |
| ------------ | -------------------- |
| id           | Primary key          |
| name         | Customer name        |
| email        | Unique email         |
| phone        | Contact number       |
| reservations | List of reservations |

---

### 4. **Reservation**

| Field     | Description                     |
| --------- | ------------------------------- |
| id        | Primary key                     |
| table     | Reserved table                  |
| customer  | Customer                        |
| startTime | Start time                      |
| endTime   | End time                        |
| guests    | Number of guests                |
| status    | PENDING / CONFIRMED / CANCELLED |

---

# 🧩 Relationships

* Restaurant **1 → n** Tables
* Table **1 → n** Reservations
* Customer **1 → n** Reservations

---

# 🔒 Business Rules

The system strictly enforces:

### 1️⃣ No double booking

A table cannot be reserved twice for overlapping times:

```
new.start < existing.end
AND
new.end > existing.start
```

### 2️⃣ Capacity check

`guests ≤ table.capacity`

### 3️⃣ Opening hours

Reservation must fall between `restaurant.openingTime` and `closingTime`.

### 4️⃣ No past reservations

Reservation start time must be in the future.

### 5️⃣ Cancellation allowed only when:

`status == PENDING` or `status == CONFIRMED`

### 6️⃣ Consistent status flow

```
PENDING → CONFIRMED → CANCELLED
CANCELLED (terminal state)
```

---

# 🌐 API Endpoints

## 📍 Restaurant API

### GET /restaurants

Get all restaurants.

### GET /restaurants/{id}

Get details (including tables).

### GET /restaurants/{id}/availability?date=YYYY-MM-DD

Returns available tables for that date & time slots.

---

## 👤 Customer API

### POST /customers

Register new customer.

### GET /customers/{id}

Get customer profile.

### GET /customers/{id}/reservations

Customer reservation history.

---

## 📅 Reservation API

### POST /reservations

Create reservation.

**Request example**

```json
{
  "customerId": 1,
  "tableId": 5,
  "startTime": "2025-12-05T18:00",
  "durationMinutes": 120,
  "guests": 4
}
```

### GET /reservations/{id}

Get reservation details.

### DELETE /reservations/{id}

Cancel reservation.

---

# ⚠️ Validation & Exception Handling

## Validation Examples

* `@NotNull`
* `@NotBlank`
* `@Email`
* `@Min`
* `@Max`
* Custom validator for time range

---

## Custom Exceptions

| Exception                       | When                      |
| ------------------------------- | ------------------------- |
| RestaurantNotFoundException     | Invalid restaurant ID     |
| TableNotFoundException          | Invalid table ID          |
| CustomerNotFoundException       | Invalid customer ID       |
| ReservationConflictException    | Double booking            |
| InvalidReservationTimeException | Outside opening hours     |
| PastReservationException        | Start time < now          |
| CapacityExceededException       | Guests > capacity         |
| CancellationNotAllowedException | Illegal reservation state |

---

## Global Exception Handler

* Uses `@RestControllerAdvice`
* Returns structured JSON error:

```json
{
  "timestamp": "2025-12-03T10:15:30",
  "status": 409,
  "error": "Reservation conflict",
  "message": "Table 5 is already booked for this time.",
  "path": "/reservations"
}
```

---

# 🧪 Tests

File: **api-tests.http**

### ✔ Happy Path

* Create customer
* Create reservation
* Check availability
* Cancel reservation
* View history

### ✔ Error Path

* Double booking
* Booking in the past
* Exceeding capacity
* Booking outside opening hours
* Invalid inputs

Example (VSCode REST Client):

```
### Create Reservation (valid)
POST http://localhost:8080/reservations
Content-Type: application/json

{
  "customerId": 1,
  "tableId": 2,
  "startTime": "2025-12-03T19:00",
  "durationMinutes": 90,
  "guests": 3
}
```



# 🧰 Tech Stack

* **Java 21**
* **Spring Boot 3.24**
* **Spring Web**
* **Spring Data JPA**
* **JPA/Hibernate**
* **H2 In-Memory Database**
* **Spring Validation**
* **Lombok**
* **RestControllerAdvice**

---


### Run with Maven

```bash
mvn spring-boot:run
click 'run' in src\main\java\com\restaurant\RestaurantApplication.java
```

### H2 Console

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:mem:restaurantdb
```


# 🏁 Conclusion

This project demonstrates:

✔ Solid understanding of **Spring Boot architecture**
✔ Ability to design real-world **REST APIs**
✔ Mastery of **Business Rule implementation**
✔ Clean code, validation, and exception handling
✔ Use of DTO patterns and layered architecture
✔ Professional-grade documentation



---

