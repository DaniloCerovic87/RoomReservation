## Setup and run the project

RoomReservation is a room reservation application that can be run using Maven or Docker Compose.

To run with Maven, configure the database connection in the application.yml file by adding the MySQL url, username, and password. Then, build and run the application using the commands ***mvn clean install***, navigate to the target folder, and run the .jar file.

Alternatively, you can use Docker Compose to start both the application and the MySQL database. Make sure Docker is installed, then run ***docker compose up --build*** in the root folder. The application will be accessible at http://localhost:8080, and the MySQL database will be available on port 3306.

To stop Docker containers, use ***docker compose down***.

## Test authentication data

The initial database seed includes demo users for local testing only. Their passwords are stored as BCrypt hashes in the seed data.

| Username | Password | Role |
| --- | --- | --- |
| `Pera` | `fon123` | `USER` |
| `Mika` | `fon345` | `ADMIN` |

To get a JWT token, call:

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

```json
{
  "username": "Pera",
  "password": "fon123"
}
```

Use the returned token when calling protected endpoints:

```http
Authorization: Bearer <token>
```

For example, AI chat requests should not include `userId`; the backend reads the authenticated user from the JWT:

```json
{
  "conversationId": "jwt-ai-test-1",
  "message": "Treba mi sala za sastanak 2026-09-28 od 10:00 do 11:00 za 8 ljudi."
}
```




