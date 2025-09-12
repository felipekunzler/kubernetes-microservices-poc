package main

import (
	"github.com/nats-io/nats.go"
	"os"
)

var redisUrl = getEnv("REDIS_URL", "localhost:6379")
var productServiceUrl = getEnv("PRODUCT_SERVICE_URL", "http://localhost:8080")
var natsServerUrl = getEnv("NATS_URL", nats.DefaultURL)

func getEnv(key, fallback string) string {
	if value, ok := os.LookupEnv(key); ok {
		return value
	}
	return fallback
}
