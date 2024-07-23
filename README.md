# Ecommerce Platform

## Project Overview
This is a multi-module microservices-based Ecommerce platform. The project is built using Spring Boot and consists of several microservices: User, Product, Order, and Payment services. Each service is documented using Swagger.

## Prerequisites
- Java 11 or later
- Docker and Docker Compose
- Maven

## Setup Instructions

### Running Locally
1. **Clone the repository**:
    ```bash
    git clone https://github.com/hazzimeh/Ecommerce-platform.git
    cd Ecommerce-platform
    ```

2. **Build the project**:
    ```bash
    mvn clean install
    ```

3. **Run the services**:
   Use the Docker Compose file to run all services.
    ```bash
    docker-compose up --build
    ```

4. **Access Swagger UI**:
    - API Gateway: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - User Service: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
    - Product Service: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
    - Order Service: [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)
    - Payment Service: [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html)

## Project Structure
- **api-gateway**: API Gateway using Spring Cloud Gateway
- **user-service**: Handles user registration and authentication
- **product-service**: Manages product information
- **order-service**: Manages orders
- **payment-service**: Manages payments

## Docker Compose
The project uses Docker Compose to manage microservices. The `docker-compose.yml` file is located at the root of the project.


