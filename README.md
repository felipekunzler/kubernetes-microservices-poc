# Kubernetes and microservices POC

## To-be architecture
![k8s-microservices-demo](https://user-images.githubusercontent.com/9336586/115996458-ba686700-a5b5-11eb-9fb4-0e0ae109906f.png)

## Microservices
* [Cart service](./cart/) written in Go and Redis
* [Product service](./product/) written in Java 11, Spring Boot and PostgreSQL
* [Frontend](./frontend/) written in Angular
* [Order service](./order) written in Java 16
* [Payment service](./payment) written in Java 16

## Running locally with Docker
* Run all services locally in Docker with `docker-compose up --build`
* Access the frontend at http://localhost:4200

## Running locally with Kubernetes
* Coming soon
