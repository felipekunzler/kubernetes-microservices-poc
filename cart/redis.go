package main

import (
	"context"
	"github.com/go-redis/redis/v8"
)

type redisStore struct {
	client  *redis.Client
	context context.Context
}

func NewRedisStore() *redisStore {
	r := &redisStore{}
	r.context = context.Background()
	r.client = redis.NewClient(&redis.Options{
		Addr:     "localhost:6379", // TODO: Get by env variable
		Password: "",
		DB:       0,
	})
	return r
}
