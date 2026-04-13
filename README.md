# Blood Bank Management System

## Description

This project is a Blood Bank Management System built using Spring Boot and MySQL.
It helps manage users, donors, blood inventory, requests, emergency alerts, and transfusions.

---

## Features

* User Management
* Donor Management
* Blood Inventory
* Blood Requests
* Emergency Alerts
* Transfusion Tracking

---

## Tech Stack

* Java (Spring Boot)
* MySQL
* Maven
* Postman

---

## How to Run

### 1. Clone the project

```bash
git clone https://github.com/your-username/your-repo.git
cd your-repo
```

### 2. Create database

```sql
CREATE DATABASE blood_bank_db;
```

### 3. Configure database

Open `application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blood_bank_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### 4. Run project

```bash
mvn spring-boot:run
```

### 5. Open

```
http://localhost:8080
```

---

## APIs (Examples)

* POST /api/users
* GET /api/users
* POST /api/blood-units
* GET /api/blood-units/inventory
* POST /api/blood-requests
* POST /api/transfusions

---

## Author

Aasritha

---

## Repository
https://github.com/Aasrithamallipudi/blood-bank-backend/new/main

