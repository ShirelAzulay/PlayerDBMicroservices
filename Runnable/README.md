```markdown
# PlayerDBMicroservice

## Description
This project is a microservice to handle player data from CSV and is built with Spring Boot. The provided docker-compose file will run the microservice along with Kafka and Kafdrop for message handling and monitoring.

## Prerequisites
- Docker
- Docker Compose

## Setup Instructions
1. Structure the project directory to include the following files:
   ```plaintext
   Runnable/
   ├── Dockerfile
   ├── docker-compose.yml
   ├── README.md
   └── playerdbmicroservice-0.0.1-SNAPSHOT.jar
   ```

2. Navigate to the project directory:
   ```sh
   cd Runnable
   ```

3. Build and run the Docker containers using Docker Compose:
   ```sh
   docker-compose up --build
   ```

4. Access the services once they are running:
    - PlayerDBMicroservice: [http://localhost:8080](http://localhost:8080)
    - Kafdrop: [http://localhost:9000](http://localhost:9000)

## Additional Information
- Ensure that your Docker daemon is running.
- The Kafka broker is configured to be accessible within the Docker network as `kafka:29092` for internal communications and as `localhost:9092` for external communications.
```