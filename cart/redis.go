package main

import (
	"context"
	"github.com/go-redis/redis/v8"
)

var redisClient *redis.Client
var ctx = context.Background()

func initialize() {
	redisClient = redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})
}
