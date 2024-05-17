# Description of API

Click the link below to access the API documentation:

[Swagger Documentation](http://localhost:8090/swagger-ui/index.html#/)

# Contributors

@Janek1010
@Kondziow
@Drillllll

# Installation

IDE:

To run the project, clone the repository and open it in an IDE (e.g., IntelliJ). Before running the project, ensure you build it using the following command:

``mvn clean package ``

DOCKER:

Firstly build the project to obtain the jar file to have the latest version of the application with a command (You don't have to download maven, just type this command):
``
.\mvnw clean package
``
Then run the following command:
``
docker-compose up
``


# About the Project
This backend application enables users to access and utilize audio content associated with real-world locations. Users can also create routes containing multiple audio descriptions of various locations, either by creating their own audio content or using content shared by other users.

# Testing
The project includes comprehensive automated tests covering most lines of code. All tests are automatically triggered upon pushing changes to the main branch.

# Technologies
- Java 17
- PostgreSQL 16.1
- Spring Boot 3.1.5
- Spring 6
- Maven
- Lombok
- Swagger
- Spring Validation
- Hibernate
- Spring Data JPA
- H2 (for testing)
- OpenCSV
- OpenAPI
- Flyway
- MapStruct
