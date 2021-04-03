package main

import (
	"encoding/json"
	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
	"net/http"
)

type cart struct {
	Id      string  `json:"id"`
	Total   float32 `json:"total"`
	Entries []entry `json:"entries"`
}

type entry struct {
	ProductId string `json:"id"`
	Quantity  int    `json:"quantity"`
}

type entryInput struct {
	ProductId string `json:"productId"`
	Quantity  int    `json:"quantity"`
	CartId    string `json:"cartId"`
}

func listCart(c echo.Context) error {
	id := c.Param("id")
	result, _ := redisClient.Get(ctx, id).Result()
	return c.String(http.StatusOK, result)
}

func postEntry(echoContext echo.Context) error {
	// Temporary input
	input := entryInput{
		ProductId: "bcd22",
		Quantity:  3,
		CartId:    "b84fc502-3806-4bf6-9d8b-06ed589d9087",
	}

	result, _ := redisClient.Get(ctx, input.CartId).Result()
	var c *cart
	if result != "" {
		err := json.Unmarshal([]byte(result), &c)
		if err != nil {
			return err
		}

		e := findEntry(c, input.ProductId)
		if e != nil {
			e.Quantity = input.Quantity
		} else {
			appendNewEntry(c, &input)
		}
	} else {
		c = new(cart)
		c.Id = uuid.New().String()
		appendNewEntry(c, &input)
	}

	cartJson, _ := json.Marshal(c)
	redisClient.Set(ctx, c.Id, cartJson, 0)

	return echoContext.JSON(http.StatusOK, c)
}

func findEntry(c *cart, productId string) *entry {
	for i := range c.Entries {
		if c.Entries[i].ProductId == productId {
			return &c.Entries[i]
		}
	}
	return nil
}

func appendNewEntry(c *cart, input *entryInput) {
	c.Entries = append(c.Entries, entry{
		ProductId: input.ProductId,
		Quantity:  input.Quantity,
	})
}
