```markdown
# PlayerDBMicroservice

## Description

This project is a microservice to handle player data from CSV and is built with Spring Boot. The provided docker-compose file will run the microservice along with Kafka and Kafdrop for message handling and monitoring.

## Prerequisites

- Docker
- Docker Compose
- Maven (for building the project)

## Setup Instructions

### Development Environment

1. Build the project using Maven
    ```sh
    mvn clean install
    ```

2. Run the application
    ```sh
    mvn spring-boot:run
    ```

3. Access the service on your `localhost`:
    - PlayerDBMicroservice: [http://localhost:8080](http://localhost:8080)

### Docker Environment

1. Build the project using Maven to create the `target` directory and JAR file
    ```sh
    mvn clean install
    ```

2. Ensure your project directory structure includes the following files:
    ```plaintext
    .
    ├── Dockerfile
    ├── docker-compose.yml
    ├── README.md
    └── target/
        └── playerdbmicroservice-0.0.1-SNAPSHOT.jar
    ```

3. Build and run the Docker containers using Docker Compose
    ```sh
    docker-compose up --build
    ```

4. Access the services once they are running:
    - PlayerDBMicroservice: [http://localhost:8080](http://localhost:8080)
    - Kafdrop: [http://localhost:9000](http://localhost:9000)

## API Endpoints

### Get Player by ID

Retrieve player information by player ID.

Request
```http
GET /api/players/{playerID}
```

Example
```http
GET http://localhost:8080/api/players/aardsda01
```

Response
```json
{
  "playerID": "aardsda01",
  "birthYear": 1981,
  "birthMonth": 12,
  "birthDay": 27,
  "birthCountry": "USA",
  "birthState": null,
  "birthCity": null,
  "deathYear": null,
  "deathMonth": null,
  "deathDay": null,
  "deathCountry": null,
  "deathState": null,
  "deathCity": null,
  "nameFirst": null,
  "nameLast": null,
  "nameGiven": null,
  "weight": null,
  "height": null,
  "bats": null,
  "throwsHand": null,
  "debut": null,
  "finalGame": null,
  "retroID": null,
  "bbrefID": null
}
```

### Get All Players with Pagination

Retrieve a paginated list of players.

Request
```http
GET /api/players?page={page}&size={size}
```

Parameters
- page: Page number (default is `0`)
- size: Number of players per page (default is `10`)

Example
```http
GET http://localhost:8080/api/players?page=0&size=2
```

Response
```json
[
  {
    "playerID": "aardsda01",
    "birthYear": 1981,
    "birthMonth": 12,
    "birthDay": 27,
    "birthCountry": "USA",
    "birthState": null,
    "birthCity": null,
    "deathYear": null,
    "deathMonth": null,
    "deathDay": null,
    "deathCountry": null,
    "deathState": null,
    "deathCity": null,
    "nameFirst": null,
    "nameLast": null,
    "nameGiven": null,
    "weight": null,
    "height": null,
    "bats": null,
    "throwsHand": null,
    "debut": null,
    "finalGame": null,
    "retroID": null,
    "bbrefID": null
  },
  {
    "playerID": "aaronha01",
    "birthYear": 1934,
    "birthMonth": 2,
    "birthDay": 5,
    "birthCountry": "USA",
    "birthState": null,
    "birthCity": null,
    "deathYear": null,
    "deathMonth": null,
    "deathDay": null,
    "deathCountry": null,
    "deathState": null,
    "deathCity": null,
    "nameFirst": null,
    "nameLast": null,
    "nameGiven": null,
    "weight": null,
    "height": null,
    "bats": null,
    "throwsHand": null,
    "debut": null,
    "finalGame": null,
    "retroID": null,
    "bbrefID": null
  }
]
```

### Reload CSV Data

Reload player data from a CSV file.

Request
```http
POST /api/players/reload
```

Example
```http
POST http://localhost:8080/api/players/reload
```

Response
```json
{
  "status": "success",
  "message": "CSV reloaded successfully",
  "timestamp": 1724409904501
}
```

## Additional Information

- Ensure that your Docker daemon is running.
- The Kafka broker is configured to be accessible within the Docker network as `kafka:29092` for internal communications and as `localhost:9092` for external communications.
```