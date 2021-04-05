package main

import (
	"encoding/json"
	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

type handler struct {
	rs *redisStore
}

type cart struct {
	Id      string  `json:"id"`
	Total   float32 `json:"total"`
	Entries []entry `json:"entries"`
}

type entry struct {
	Id        int    `json:"id"`
	ProductId string `json:"productId"`
	Quantity  int    `json:"quantity"`
}

// Creates an empty cart.
func (h *handler) createCart(echoContext echo.Context) error {
	c := new(cart)
	c.Id = uuid.New().String()
	cartJson, _ := json.Marshal(c)
	err := h.rs.client.Set(h.rs.context, c.Id, cartJson, 0).Err()
	if err != nil {
		return err
	}
	return echoContext.JSON(http.StatusOK, c)
}

// Lists a cart by an id.
func (h *handler) listCart(c echo.Context) error {
	id := c.Param("id")
	result, err := h.rs.client.Get(h.rs.context, id).Result()
	if err != nil {
		return err
	}
	return c.Blob(http.StatusOK, echo.MIMEApplicationJSONCharsetUTF8, []byte(result))
}

// Creates a new entry to a given cart.
func (h *handler) postEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("id")

	input := &entry{}
	if err := echoContext.Bind(input); err != nil && input == nil {
		return err
	}
	result, err := h.rs.client.Get(h.rs.context, cartId).Result()
	if err != nil {
		return err
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	appendNewEntry(c, input)
	h.updateInRedis(c)

	return echoContext.JSON(http.StatusOK, c)
}

func (h *handler) updateInRedis(c *cart) {
	cartJson, _ := json.Marshal(c)
	h.rs.client.Set(h.rs.context, c.Id, cartJson, 0)
}

// Updates an existing entry. Mostly used for changing the product quantity.
func (h *handler) patchEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("cartId")
	entryId := echoContext.Param("entryId")
	entryIdInt, _ := strconv.Atoi(entryId)

	input := &entry{}
	if err := echoContext.Bind(input); err != nil && input == nil {
		return err
	}
	result, err := h.rs.client.Get(h.rs.context, cartId).Result()
	if err != nil {
		return err
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	updateEntry(c, input, entryIdInt)
	h.updateInRedis(c)

	return echoContext.JSON(http.StatusOK, c)
}

// Deletes an entry by id.
func (h *handler) deleteEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("cartId")
	entryId := echoContext.Param("entryId")
	entryIdInt, _ := strconv.Atoi(entryId)

	result, err := h.rs.client.Get(h.rs.context, cartId).Result()
	if err != nil {
		return err
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	deleteEntryById(c, entryIdInt)
	h.updateInRedis(c)

	return echoContext.JSON(http.StatusOK, c)
}

func deleteEntryById(c *cart, id int) {
	_, i := findEntry(c, id)
	c.Entries = append(c.Entries[:i], c.Entries[i+1:]...)
}

func updateEntry(c *cart, input *entry, id int) {
	e, _ := findEntry(c, id)
	e.Quantity = input.Quantity
}

func findEntry(c *cart, id int) (*entry, int) {
	for i := range c.Entries {
		if c.Entries[i].Id == id {
			return &c.Entries[i], i
		}
	}
	return nil, -1
}

func appendNewEntry(c *cart, input *entry) {
	var maxId int
	for _, e := range c.Entries {
		if e.Id > maxId {
			maxId = e.Id
		}
	}

	c.Entries = append(c.Entries, entry{
		Id:        maxId + 1,
		ProductId: input.ProductId,
		Quantity:  input.Quantity,
	})
}
