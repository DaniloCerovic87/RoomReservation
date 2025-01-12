## Setup and run the project

RoomReservation is a room reservation application that can be run using Maven or Docker Compose.

To run with Maven, configure the database connection in the application.yml file by adding the MySQL url, username, and password. Then, build and run the application using the commands ***mvn clean install***, navigate to the target folder, and run the .jar file.

Alternatively, you can use Docker Compose to start both the application and the MySQL database. Make sure Docker is installed, then run ***docker compose up --build*** in the root folder. The application will be accessible at http://localhost:8080, and the MySQL database will be available on port 3306.

To stop Docker containers, use ***docker compose down***.




