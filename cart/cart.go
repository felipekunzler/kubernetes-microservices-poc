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
	Id        int `json:"id"`
	ProductId string `json:"productId"`
	Quantity  int    `json:"quantity"`
}

func createCart(echoContext echo.Context) error {
	c := new(cart)
	c.Id = uuid.New().String()
	cartJson, _ := json.Marshal(c)
	err := redisClient.Set(ctx, c.Id, cartJson, 0).Err()
	if err != nil {
		return err
	}
	return echoContext.JSON(http.StatusOK, c)
}

func listCart(c echo.Context) error {
	id := c.Param("id")
	result, err := redisClient.Get(ctx, id).Result()
	if err != nil {
		return err
	}
	return c.Blob(http.StatusOK, echo.MIMEApplicationJSONCharsetUTF8, []byte(result))
}

func postEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("id")

	input := &entry{}
	if err := echoContext.Bind(input); err != nil && input == nil {
		return err
	}
	result, err := redisClient.Get(ctx, cartId).Result()
	if err != nil {
		return err
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	appendNewEntry(c, input)

	cartJson, _ := json.Marshal(c)
	redisClient.Set(ctx, c.Id, cartJson, 0)

	return echoContext.JSON(http.StatusOK, c)
}

func findEntry(c *cart, id int) *entry {
	for i := range c.Entries {
		if c.Entries[i].Id == id {
			return &c.Entries[i]
		}
	}
	return nil
}

func appendNewEntry(c *cart, input *entry) {
	var maxId int
	for _, e := range c.Entries {
		if e.Id > maxId {
			maxId = e.Id
		}
	}

	c.Entries = append(c.Entries, entry{
		Id: maxId + 1,
		ProductId: input.ProductId,
		Quantity:  input.Quantity,
	})
}
