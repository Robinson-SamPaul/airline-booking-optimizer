<h1 align="center">✈️ Flight Overbooking Optimization System</h1>

<p align="center">
  Optimize airline seat overbooking using predictive analytics and real-time alerts.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/status-in--progress-yellow"  alt="status"/>
  <img src="https://img.shields.io/badge/services-3-blue"  alt="services"/>
  <img src="https://img.shields.io/badge/database-H2-lightgrey"  alt="database"/>
  <img src="https://img.shields.io/badge/license-MIT-green"  alt="license"/>
</p>

---

## 🚀 Project Overview

A microservices-based system designed to predict no-show passengers, optimize overbooking, and send real-time alerts. Built with **Spring Boot**, **FastAPI**, and **Quarkus**, using **H2** for fast in-memory development.

---

## 🧱 Tech Stack

| Layer             | Technology            |
|-------------------|-----------------------|
| Booking Service   | Spring Boot (Java 17) |
| Analytics Service | FastAPI (Python 3.10) |
| Notification      | Quarkus               |
| Database          | H2 (in-memory)        |
| APIs              | REST                  |

---

## 🧩 Microservices

| Service                   | Description                                                                 |
|---------------------------|-----------------------------------------------------------------------------|
| 📘 `booking-service`      | Manages bookings & interacts with analytics to determine overbooking safety |
| 📊 `analytics-service`    | Predicts no-show passengers based on historical trends                      |
| 📢 `notification-service` | Sends alerts when overbooking is risky                                      |

---

<details>
  <summary>📂 Folder Structure</summary>

    flight-overbooking-system/ </br>
    ├── booking-service/ # Spring Boot with H2 </br>
    ├── analytics-service/ # FastAPI </br>
    ├── notification-service/ # Quarkus </br>
    └── README.md

</details>

---

## ⚙️ How to Run

### 🧪 Prerequisites

- Java 17+
- Python 3.10+
- Maven or Gradle
- IDE: IntelliJ / VS Code / PyCharm

---

### 🟦 1. Booking Service

```bash
    cd booking-service
    ./mvnw spring-boot:run
```

#### H2 Console: http://localhost:8082/h2-console

### 🟨 2. Analytics Service (FastAPI)

```bash
    cd analytics-service
    python -m venv .venv
    source .venv/bin/activate   # or .venv\Scripts\activate on Windows
    pip install -r requirements.txt
    uvicorn main:app --reload
```

### 🟥 3. Notification Service (Quarkus)

```bash
    cd notification-service
  ./mvnw quarkus:dev
```

---

## 🔌 API Documentation

### 📘 Booking Service (Spring Boot)

#### POST 
    /booking

#### Description: 
    Create a new booking for a flight.

#### Request Body:

```json
{
  "name": "John Doe",
  "mail": "john@example.com",
  "flightId": 101
}
```

#### Response Body

```json
{
  "id": 1,
  "name": "John Doe",
  "mail": "john@example.com",
  "flightId": 101,
  "bookingStatus": "CONFIRMED",
  "bookedTime": "13:45:00"
}

```

---

#### GET 
    /booking

#### Description: 
    Get all bookings in the system.

#### Endpoint:
    http://localhost:8082/booking/

#### Response: 
    List of Booking objects.

---

#### GET 
    /booking/{id}

#### Description: 
    Get a booking by its unique ID.

#### Example:
    http://localhost:8082/booking/1

#### Response:

```json
{
  "id": 1,
  "name": "John Doe",
  "mail": "john@example.com",
  "flightId": 101,
  "bookingStatus": "CONFIRMED",
  "checkInStatus": "NO_SHOW",
  "bookedTime": "13:45:00"
}
```

---

#### GET 
    /booking/flight/{id}

#### Description: 
    Get all bookings for a specific flight.

#### Example:
    http://localhost:8082/booking/flight/101

#### Response: 
    List of Booking details.

---

#### PATCH 
    /booking/check-in/{id}

#### Description: 
    Update the check-in status of a booking.

#### Example:
    http://localhost:8082/booking/check-in/1
(Marks passenger as "SHOW")

---

#### DELETE 
    /booking/{id}

#### Description: 
    Delete a booking by ID.

#### Example:
    http://localhost:8082/1

---

#### DELETE 
    /booking

#### Description: 
    Delete all bookings in the system.

#### Endpoint:
    http://localhost:8082/

---

#### POST 
    /flight/schedule

#### Description: 
    Schedule a new flight with details like take-off time, seat info.

#### Endpoint:
    http://localhost:8082/flight/schedule

#### Request Body:

```json
{
    "flightId": 101,
    "flightName": "AI Express",
    "actualSeats": 100,
    "predictedSeats": 95,
    "bookedSeats": 105,
    "takeOffTime": "2025-06-30T14:00:00"
}
```

#### Response: 
    Full Flight object.

---

#### GET 
    /flight

#### Description: 
    Get all scheduled flights.

#### Endpoint:
    http://localhost:8082/flight

#### Response: 
    List of Flight objects.

---

#### POST 
    /flight/create/

#### Description: 
    Add a new aircraft to the system.

#### Endpoint:
    http://localhost:8082/flight/create

#### Request Body:

```json
{
    "planeName": "Boeing 737",
    "source": "Delhi",
    "destination": "Mumbai",
    "totalSeats": 180
}
```

#### Response: 
    Full Plane object with auto-generated flightId.

---

### 📢 Notification Service (Quarkus)

#### POST
    /email/send

#### Description:
    Sends a notification email (or simulated alert) for a list of flight bookings — typically used to notify affected passengers in overbooking or disruption scenarios.

#### Endpoint:
    http://localhost:8084/email/send

#### Content-Type:
    application/json

#### Request Body

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "mail": "john@example.com",
    "flightId": 123,
    "bookingStatus": "CONFIRMED",
    "checkInStatus": "NOT_CHECKED_IN",
    "bookedTime": "14:30:00"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "mail": "jane@example.com",
    "flightId": 123,
    "bookingStatus": "CANCELLED",
    "checkInStatus": "NOT_CHECKED_IN",
    "bookedTime": "15:45:00"
  }
]
```

#### Response  
    "Sent!"

---

### 📊 Analytics Service (FastAPI)

#### POST 
    /predict-seats

#### Description:
    Predict the number of seats that can be safely overbooked based on historical booking trends using a linear regression model.

#### Endpoint:
    http://localhost:8000/predict-seats

#### Content-Type:
    application/json

#### Request Body 

```json
[
    {
        "actualSeats": 100,
        "predictedSeats": 95,
        "bookedSeats": 110,
        "filledSeats": 94
    },
    {
        "actualSeats": 100,
        "predictedSeats": 97,
        "bookedSeats": 115,
        "filledSeats": 95
    }
]
```
⚠️ Minimum 2 records are required.

#### Success Response:

```json
{
    "predictedSeats": 96,
    "note": "Predicted based on historical average inputs"
}
```
#### Error Response:

```json
{
    "error": "Need at least 2 data points to train model"
}
```

---

## 🌱 Future Enhancements
- ✅ Add JWT-based authentication
- 🔄 Replace REST with Kafka messaging
- 🐳 Dockerize all services
- 📊 Create a frontend dashboard
- 🧠 ML model improvements

---

## 🤝 Contributing
Contributions are welcome!
Please fork the repo, create a new branch, and submit a pull request.

---

## 📃 License
Licensed under the **MIT License**.

---

## 👨‍💻 Author
**Robinson Sampaul E** </br>
*💼 Java Backend Developer | 🛠 Microservices Enthusiast | 🧠 Python + Quarkus Explorer* </br>
📬 [LinkedIn](https://www.linkedin.com/in/robinson-sampaul-e/) | 🌐 [GitHub](https://github.com/Robinson-SamPaul) | 📧 [Mail](mailto:robinpaulsam264@gmail.com) 