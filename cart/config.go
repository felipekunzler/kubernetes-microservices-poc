package main

import "os"

var redisUrl = getEnv("REDIS_URL", "localhost:6379")
var productServiceUrl = getEnv("PRODUCT_SERVICE_URL", "http://localhost:8080")
var rabbitmqUrl = getEnv("RABBITMQ_URL", "amqp://guest:guest@localhost:5672")

func getEnv(key, fallback string) string {
	if value, ok := os.LookupEnv(key); ok {
		return value
	}
	return fallback
}
