# Kubernetes and microservices POC

## To-be architecture
![k8s-microservices-demo](https://user-images.githubusercontent.com/9336586/115996458-ba686700-a5b5-11eb-9fb4-0e0ae109906f.png)

## Microservices
* [Cart service](./cart/) written in Go and Redis
* [Product service](./product/) written in Java 21, Spring Boot and PostgreSQL
* [Frontend](./frontend/) written in Angular 18
* [Order service](./order) written in Java 21

## Running locally with Docker
* Run all services locally in Docker with `docker-compose up --build`
* Access the frontend at http://localhost:80

## Running locally with Kubernetes
* Create a kubernetes cluster with `k3d`. Ports 80 and 443 will be accessible from the host machine and disable traefik as we'll use nginx as our ingress
```
k3d cluster create k8s-poc \
-p 80:80@loadbalancer \
-p 443:443@loadbalancer \
--k3s-arg "--disable=traefik@server:*"
```
* Deploy all services with `kubectl apply -f deployment/kubernetes-vanilla/`
* Pods startup can be watched with `watch kubectl get pods -A`
* Access the frontend at http://localhost:80
* Logs can be checked with `kubectl logs -l 'app in (product, frontend, order, cart)' -f`

## Running locally with Kubernetes and Istio
* Coming soon


## Storefront screenshots
![](https://user-images.githubusercontent.com/9336586/120122720-d5b02e80-c180-11eb-9b1c-446a26c4b58a.png)
![](https://user-images.githubusercontent.com/9336586/120122722-d8128880-c180-11eb-8175-97d2e17201ed.png)
![](https://user-images.githubusercontent.com/9336586/120122723-d943b580-c180-11eb-835d-4011097b7344.png)

## Setting up dashboard for Kubernetes
```
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard
kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath={".data.token"} | base64 -d
kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443
https://localhost:8443
```