# Kubernetes and microservices POC

# To-be architecture
![k8s-microservices-demo](https://user-images.githubusercontent.com/9336586/113518563-71486880-955d-11eb-904f-141e09972d8a.png)

## Microservices
* [Cart service](./cart/) written in Go and Redis
* [Product service](./product/) written in Java 11, Spring Boot and PostgreSQL

## Todo
* Products
    * Generate API schema on build
    * Proper error msg on 404
    * Postegres on Docker/k8s
    
* Cart
  * Correct HTTP response codes
  * Refactor
  * Handle all errors
