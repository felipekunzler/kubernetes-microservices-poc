# Kubernetes and microservices POC

## To-be architecture
![k8s-microservices-demo](https://user-images.githubusercontent.com/9336586/113788859-53713400-9714-11eb-8855-4da315f48581.png)

## Microservices
* [Cart service](./cart/) written in Go and Redis
* [Product service](./product/) written in Java 11, Spring Boot and PostgreSQL

## Running locally with Docker
* Run all services locally in Docker with `docker-compose up --build`

## Running locally with Kubernetes
* Coming soon

## Todo
* Products
    * Generate API schema on build
    * Proper error msg on 404
    * Postegres on Docker/k8s
