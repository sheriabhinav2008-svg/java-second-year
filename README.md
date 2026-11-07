# Campus Club Hub — College Club & Fest Management

A responsive student-facing college club and fest management web application built with **Java 17, Spring Boot, JDBC, MySQL, HTML, CSS and JavaScript**.

Students sign in with their registered name and student ID, explore campus events, register for events, join clubs, view event schedules, and see their own coordinator-verified attendance history.

## Features

- Student login validated against the MySQL student directory.
- Personalized dashboard with registrations, attendance, club memberships and upcoming-event summaries.
- Event discovery with Technical, Cultural, Sports and Creative filters.
- Event registration with duplicate-registration and capacity checks.
- Club directory and membership joining with duplicate-membership protection.
- Schedule table showing event date, start/end time and venue.
- Personal attendance history; students cannot mark their own attendance.
- Student profile page.
- Responsive layout for desktop, tablet and mobile.
- REST API backed by Spring JDBC and MySQL.

> **Security note:** Name + student ID is a lightweight college-project demonstration login, not production-grade authentication. For real deployment, add password/OTP verification, server-side sessions or JWT, role-based administrator/coordinator accounts, CSRF protection, rate limiting and audit logs. Do not expose coordinator attendance-management endpoints to students.

## Technology

| Layer | Technology |
|---|---|
| Frontend | HTML5, CSS3, JavaScript |
| Backend | Java 17, Spring Boot 3.4 |
| Database access | Spring JDBC (JDBC) |
| Database | MySQL 8+ |
| Build | Maven |

## Project structure

```text
campus-club-hub/
├── database/
│   └── schema.sql
├── src/main/java/com/college/campushub/
│   └── CampusHubApplication.java
├── src/main/resources/
│   ├── application.properties.example
│   └── static/
│       ├── index.html
│       ├── styles.css
│       └── app.js
├── pom.xml
└── README.md
```

## Requirements

- Java Development Kit (JDK) 17 or later
- MySQL Server 8 or later
- Apache Maven 3.8+
- Git (optional)

## Run locally on Windows

### 1. Clone the repository

```bash
git clone https://github.com/sheriabhinav2008-svg/java-second-year.git
cd java-second-year
```

### 2. Create the database

Open MySQL Workbench or the MySQL command line and execute the complete script:

```sql
SOURCE database/schema.sql;
```

The script creates the database, tables, foreign keys and sample records.

### 3. Configure database credentials

Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`.

Update the username and password for your local MySQL installation. Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/college_club_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.sql.init.mode=never
server.port=8080
```

Never commit real database passwords to a public repository. For deployment, use environment variables or a secrets manager.

### 4. Start the application

From the project root:

```bash
mvn spring-boot:run
```

Wait until Spring Boot reports that the application has started.

### 5. Open the website

Visit **http://localhost:8080**

Demo student:
- Student name: `Abhinav Reddy`
- Student ID: `23CSE001`

Another sample:
- Student name: `Kiran Kumar`
- Student ID: `23ECE014`

These sample records are inserted by `database/schema.sql`.

## API overview

All endpoints are served from `/api`.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/login` | Verify student name and ID |
| GET | `/dashboard/{studentId}` | Personal dashboard counts |
| GET | `/events` | List events |
| POST | `/events/{eventId}/register` | Register student for an event |
| GET | `/clubs` | List clubs |
| POST | `/clubs/{clubId}/join` | Join a club |
| GET | `/schedules` | Published event schedules |
| GET | `/attendance/{studentId}` | Student's attendance history |
| GET | `/registrations/{studentId}` | Student's event registrations |
| GET | `/profile/{studentId}` | Student profile |
| GET | `/health` | Health check |

## Attendance workflow

Attendance is deliberately read-only in the student portal. An authorized event coordinator should verify a participant (for example, using a QR code or roster) and record PRESENT/ABSENT in a protected coordinator/admin workflow. The attendance table and student endpoint are already included; a production deployment must add authenticated, role-protected coordinator operations before using real student data.

## Troubleshooting

- **Access denied for MySQL:** check the username and password in `application.properties`.
- **Unknown database:** run `database/schema.sql` first.
- **Port 8080 already in use:** change `server.port` in `application.properties`.
- **Java version error:** run `java -version` and confirm JDK 17+.
- **Maven not recognized:** install Maven and add its `bin` directory to PATH, or use the Maven wrapper if one is added.

## Future enhancements

- Administrator and coordinator authentication with role-based access.
- Protected attendance marking and QR-code check-in.
- Competition scoring, rankings and leaderboards.
- CSV/Excel/PDF reporting and participation certificates.
- Email notifications and event schedule change alerts.
- Password/OTP login and audit logging.

## License

This project is provided for educational and college-project use. Add an institutional privacy policy and obtain approval before using real student data.
