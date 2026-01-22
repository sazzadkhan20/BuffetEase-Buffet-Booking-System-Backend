# BuffetEase-Buffet-Booking-System-Backend

**BuffetEase** is a **Spring Boot Web API** for managing buffet bookings for restaurants.  
This backend application is built using **Java 21, Spring Boot 4.0.1, Maven**, and follows a **layered architecture** with **JWT-based security**, supporting **ADMIN** and **CUSTOMER** roles.

---

## 📌 Project Overview

BuffetEase is designed to handle:

- **User Management** (Admin & Customers)  
- **Buffet Packages** (Lunch, Dinner, Weekend Specials, Corporate Buffets)  
- **Buffet Scheduling** (Per date availability, capacity control)  
- **Bookings** (Multiple booking statuses: PENDING, CONFIRMED, CANCELLED, COMPLETED, REJECTED)  
- **Payments** (Optional: supports multiple payment methods)  
- **JWT Security** (Authentication & Authorization)  
- **Pagination & Sorting** using Spring JDBC  
- **Layered Architecture** for clean separation of concerns  

---

## 🏗 Architecture

The project follows a **3-layer architecture**:

```

Controller Layer (API Endpoints)        -> Handles HTTP requests/responses
Service Layer (Business Logic)         -> Contains core application logic
Repository/DAO Layer (Data Access)     -> Handles database operations using Spring JDBC

```

- **Controller Layer**: REST endpoints for users, bookings, buffet packages  
- **Service Layer**: Handles business rules, validation, and transaction management  
- **Repository Layer**: Uses **NamedParameterJdbcTemplate** for CRUD and custom queries  
- **DTOs**: Used for request/response mapping  
- **Exception Handling**: Global exceptions via `@ControllerAdvice`  

---

## 💻 Technology Stack

| Layer/Aspect               | Technology/Tool             |
|-----------------------------|----------------------------|
| Backend Framework           | Spring Boot 4.0.1           |
| Language                    | Java 21                     |
| Build Tool                  | Maven                       |
| Database                    | MySQL / MariaDB             |
| Database Access             | Spring JDBC (NamedParameterJdbcTemplate) |
| Security                    | JWT Token-based             |
| Validation                  | Jakarta Validation          |
| Version Control             | Git/GitHub                  |
| API Format                  | JSON                        |

---

## 🧩 Features

### Core Features (All Users)
- Registration
- Login
- Profile View & Update
- Change Password
- Forget Password

### Domain-Specific Features

**Admin (Full Access)**  
- Manage buffet packages (CRUD)  
- Schedule buffet availability  
- View all bookings & update status  
- Manage users (activate/deactivate)  

**Customer**  
- Browse buffet packages  
- Book a buffet (with guest count)  
- View own bookings & status  
- Cancel bookings  

---

## 📂 Database Design

- **roles** – Stores user roles (ADMIN / CUSTOMER)  
- **users** – Stores user profiles  
- **user_credentials** – Stores authentication info (email/password)  
- **buffet_packages** – Stores buffet package details  
- **buffet_schedules** – Stores date-wise availability  
- **bookings** – Stores customer bookings  
- **booking_payments** – Optional table for payment records  

> Database follows **normalized structure** with foreign keys for integrity.

---

## 🔐 Security

- **JWT-based Authentication**  
- Role-based Authorization (ADMIN / CUSTOMER)  
- Passwords stored using **BCrypt hashing**  
- Active/Inactive user handling  

---

## 📦 Project Structure

```

BuffetEase-Backend/
│
├── src/main/java/com/buffetease/
│   ├── controller/        # REST API endpoints
│   ├── service/           # Business logic
│   ├── repository/        # JDBC repositories
│   ├── dto/               # Request/Response DTOs
│   ├── entity/            # Domain entities (POJOs)
│   ├── exception/         # Global exception handling
│   └── security/          # JWT auth & filters
│
├── src/main/resources/
│   ├── application.yml    # Spring Boot config
│   └── sql/               # Optional SQL scripts
│
├── pom.xml
└── README.md

````

---

## 🚀 Getting Started

### Prerequisites

- Java 21  
- Maven 3.9+  
- MySQL or MariaDB database  
- IDE (IntelliJ, VS Code, Eclipse, etc.)

### Setup Steps

1. Clone the repo

```bash
git clone https://github.com/YourUsername/BuffetEase-Buffet-Booking-System-Backend.git
cd BuffetEase-Buffet-Booking-System-Backend
````

2. Configure **application.yml** with database credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/buffet_booking_db
    username: root
    password: password
  jdbc:
    template:
      named-parameters: true
```

3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
```

5. API will be available at:

```
http://localhost:8090/api
```

---

```markdown
## 🗄 Database Overview

The BuffetEase backend uses a **MySQL / MariaDB** database named `buffet_booking_db`.  
The database follows a **normalized, layered design**, supporting **ADMIN** and **CUSTOMER** roles, buffet packages, schedules, and bookings.  

### 1️⃣ Roles Table

Stores system roles (ADMIN / CUSTOMER)

| Column | Type | Description |
|--------|------|------------|
| id | INT, PK | Role ID |
| role_name | VARCHAR(50) UNIQUE | Role Name (ADMIN / CUSTOMER) |

**Sample Data:** ADMIN, CUSTOMER

---

### 2️⃣ Users Table

Stores user profiles (no password here)

| Column | Type | Description |
|--------|------|------------|
| id | INT, PK | User ID |
| name | VARCHAR(120) | Full Name |
| phone | VARCHAR(20) | Contact Number |
| role_id | INT, FK → roles(id) | Role of the user |
| is_active | BOOLEAN | Account status |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last updated timestamp |

---

### 3️⃣ User Credentials Table

Stores authentication data separately

| Column | Type | Description |
|--------|------|------------|
| credential_id | INT, PK | Credential ID |
| email | VARCHAR(120) UNIQUE | User email |
| password | VARCHAR(255) | BCrypt-hashed password |
| last_login | TIMESTAMP | Last login time |
| user_id | INT, FK → users(id) | Link to user profile |

---

### 4️⃣ Buffet Packages Table

Stores different buffet types

| Column | Type | Description |
|--------|------|------------|
| id | INT, PK | Buffet Package ID |
| package_name | VARCHAR(120) | Name of the buffet |
| description | TEXT | Buffet description |
| price_per_person | DECIMAL(10,2) | Price per person |
| start_time | TIME | Buffet start time |
| end_time | TIME | Buffet end time |
| max_capacity | INT | Maximum guests allowed |
| is_active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation timestamp |

---

### 5️⃣ Buffet Schedules Table

Stores buffet availability per date

| Column | Type | Description |
|--------|------|------------|
| id | INT, PK | Schedule ID |
| buffet_package_id | INT, FK → buffet_packages(id) | Linked buffet package |
| buffet_date | DATE | Date of buffet |
| available_capacity | INT | Remaining seats |
| booking_cutoff_time | TIME | Latest booking time allowed |
| is_open | BOOLEAN | Open/Closed status |

---

### 6️⃣ Bookings Table

Stores customer bookings

| Column | Type | Description |
|--------|------|------------|
| id | INT, PK | Booking ID |
| user_id | INT, FK → users(id) | Customer who booked |
| buffet_schedule_id | INT, FK → buffet_schedules(id) | Buffet schedule booked |
| number_of_guests | INT | Number of guests |
| total_price | DECIMAL(10,2) | Total booking price |
| booking_status | VARCHAR(30) | Booking status (PENDING, CONFIRMED, CANCELLED, REJECTED, COMPLETED) |
| booking_time | TIMESTAMP | Time of booking |

---

### 7️⃣ Booking Payments Table (Optional)

Stores payment information for bookings

| Column | Type | Description |
|--------|------|------------|
| id | INT, PK | Payment ID |
| booking_id | INT, FK → bookings(id) | Booking linked to payment |
| payment_method | VARCHAR(50) | Payment method (CASH, CARD, BKASH, NAGAD) |
| payment_status | VARCHAR(30) | Payment status (PAID / PENDING) |
| amount | DECIMAL(10,2) | Paid amount |
| payment_time | TIMESTAMP | Payment timestamp |

---

### 🔗 Relationships Overview

```

roles ───< users ───< user_credentials
|
└───< bookings >─── buffet_schedules >── buffet_packages
|
└── booking_payments

```

**Highlights:**

- Users are linked to **roles** and **credentials** separately.  
- Admins have full access; customers are restricted by role.  
- Buffet schedules link packages to specific dates with capacity management.  
- Bookings are connected to both users and buffet schedules.  
- Payments are optional but included for professionalism.  
```

---

## 🤝 Team

| Name           | Role                  | Responsibilities                             |
| -------------- | --------------------- | ---------------------------------------------|
| Md Sazzad Khan | Project Coordinator   | Backend architecture, security, APIs         |
| Abir Ghosh     |  Backend Developer    | Database design, APIs,repository, DTO mapping|

---

## 📌 Notes

* Project uses **Spring JDBC**, **manual entity-DTO mapping**
* Implements **JWT security** for role-based access
* Designed for **3-day project completion**
* No frontend included (backend API only)

---

## 🌟 Future Enhancements

* Multi-restaurant support (SaaS-ready)
* Email notifications (booking confirmation, cancellations)
* Online payment integration
* Admin dashboard for analytics

---

## 📄 License

MIT License © 2026 Md Sazzad Khan & Abir Ghosh
