# MeteoService

MeteoService is a microservice designed to fetch and process weather data using the Open Meteo API. It integrates with Kafka for data streaming and MongoDB for data persistence.

## Key Features

- **Kafka Integration**: Publish temperature data to a Kafka topic (`my-Topic`).
- **MongoDB Storage**: Store temperature data in MongoDB.
- **Open Meteo API**: Fetch real-time weather data from the Open Meteo API.
- **Swagger Documentation**: Explore the API endpoints using Swagger UI.
- **Postman Collection**: Test the API with a provided Postman collection.

## Prerequisites

- Docker and Docker Compose
- Java 17+
- Apache Kafka
- MongoDB

## Running the Project

To run the project, follow these steps:

### 1.  Environment Variables

Make sure the following environment variables are correctly set before running the project:

    MongoDB URI: mongodb://localhost:27017/meteoservice
    Server Port: 8080
    Open Meteo API URL: https://api.open-meteo.com/v1/forecast
    Kafka Bootstrap Servers: kafka:9092
    Swagger UI Path: /swagger-ui.html

### 2. Compile project

To compile the project, run the following command in the project's code directory:

```bash
mvn clean install
```

### 3. Start Services with Docker

To bring up all required services, including MongoDB, Kafka, and Kafdrop, run the following command in the project's code directory:

```bash
docker-compose up -d
```
This will start the following services:

- MongoDB: A database for storing weather data.
- Zookeeper and Kafka: Required services for Kafka communication.
- Kafdrop: A web interface for monitoring Kafka topics. 
- MeteoService: The main Spring Boot microservice application.

### 4. Running the Kafka Consumer

To consume messages produced by the MeteoService, manually start the Kafka consumer by running the following command in code folder:

```bash
java -jar code/SpringBootConsumer-0.0.1-SNAPSHOT.jar
```

The Kafka consumer listens for messages on the topic my-Topic with the following configuration:

    clientId: consumer-my-group-id-1
    groupId: my-group-id

### 5. Access Swagger UI

Once the services are running, you can access the API documentation through Swagger UI at: http://localhost:8080/swagger-ui/index.html

## Postman Collection

A Postman collection is provided in the /postman directory. You can import this collection into Postman to run predefined API requests and validate the functionality of the endpoints.
## Kafka Monitoring

Kafdrop is available for monitoring Kafka topics, providing insights into the published messages. You can access Kafdrop at: http://localhost:9000

## Project Structure

The project is organized into four main layers, each with clear responsibilities:

- Domain Layer: Defines the core business logic and models through entities and ports (interfaces).
- Application Layer: Implements use cases and orchestrates the business logic.
- Adapter Layer: Handles input/output, translating between external systems and the internal domain.
- Infrastructure Layer: Provides the technical implementation for database access, external APIs, and messaging.
- Boot: This layer contains the **application configuration** and initializes the necessary services. It defines configurations for infrastructure, exception handling, and other runtime settings.

## Tests

The project includes various tests to ensure proper functionality:
 - Unit Tests: These tests validate individual components of the system.
 - Integration Tests: Using Karate, these tests perform end-to-end testing of the application to ensure that all components work together as expected.
