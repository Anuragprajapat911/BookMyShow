🎬 BookMyShow Backend Application
📌 Overview

This project is a backend system inspired by BookMyShow, designed to handle movie ticket booking operations. It provides secure APIs for managing movies, theatres, shows, users, and bookings.

🚀 Features
🔐 JWT-based Authentication & Authorization
🎭 Movie and Theatre Management
🕒 Show Scheduling System
🎟️ Ticket Booking with Seat Handling
👤 User Management
📦 RESTful API Architecture
🛠️ Tech Stack
Backend: Java, Spring Boot
Security: Spring Security, JWT
Database: MySQL
ORM: JPA (Hibernate)
Build Tool: Maven
Version Control: Git
⚙️ Setup Instructions
1. Clone the Repository
git clone https://github.com/your-username/bookmyshow-backend.git
cd bookmyshow-backend
2. Configure Database

Update your application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/bookmyshow
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
3. Run the Application
mvn spring-boot:run
🔑 API Highlights
POST /auth/register → Register user
POST /auth/login → Login and get JWT
GET /movies → Fetch all movies
POST /booking → Book tickets
📂 Project Structure
src/main/java/com/example/bookmyshow
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 └── config
🔐 Security
Implemented JWT-based authentication
Role-based access control for users and admins
📌 Future Improvements
Payment Gateway Integration
Seat Selection UI Integration
Email Notifications
Docker Deployment
