# Kubernetes and microservices POC

## To-be architecture
![k8s-microservices-demo](https://user-images.githubusercontent.com/9336586/115996458-ba686700-a5b5-11eb-9fb4-0e0ae109906f.png)

## Microservices
* [Cart service](./cart/) written in Go and Redis
* [Product service](./product/) written in Java 11, Spring Boot and PostgreSQL
* [Frontend](./frontend/) written in Angular
* [Order service](./order) written in Java 16

## Running locally with Docker
* Run all services locally in Docker with `docker-compose up --build`
* Access the frontend at http://localhost:80

## Running locally with Kubernetes
* Create the cluster with `k3d`
```
k3d cluster create k8s-poc \
-p 80:80@loadbalancer \
-p 443:443@loadbalancer \
--k3s-server-arg "--no-deploy=traefik"
```

## Storefront screenshots
![](https://user-images.githubusercontent.com/9336586/120122720-d5b02e80-c180-11eb-9b1c-446a26c4b58a.png)
![](https://user-images.githubusercontent.com/9336586/120122722-d8128880-c180-11eb-8175-97d2e17201ed.png)
![](https://user-images.githubusercontent.com/9336586/120122723-d943b580-c180-11eb-835d-4011097b7344.png)
