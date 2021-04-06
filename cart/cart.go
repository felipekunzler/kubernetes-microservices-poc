package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

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

type handler struct {
	redis *redisStore
}

// Lists a cart by an id.
// GET /cart/:id
func (h *handler) listCart(c echo.Context) error {
	id := c.Param("id")

	result, err := h.redis.client.Get(h.redis.context, id).Result()
	if err != nil {
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found.", id))
	}

	return c.Blob(http.StatusOK, echo.MIMEApplicationJSONCharsetUTF8, []byte(result))
}

// Creates an empty cart.
// POST /cart
func (h *handler) createCart(echoContext echo.Context) error {
	c := new(cart)
	c.Id = uuid.New().String()

	cartJson, err := json.Marshal(c)
	if err != nil {
		return err
	}

	err = h.redis.client.Set(h.redis.context, c.Id, cartJson, 0).Err()
	if err != nil {
		return err
	}

	return echoContext.JSON(http.StatusCreated, c)
}

// Creates a new entry to a given cart.
// POST /cart/:id/entry
func (h *handler) postEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("id")

	input := &entry{}
	if err := echoContext.Bind(input); err != nil {
		return err
	}

	result, err := h.redis.client.Get(h.redis.context, cartId).Result()
	if err != nil {
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found.", cartId))
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	appendNewEntryToCart(c, input)
	if err = h.updateInRedis(c); err != nil {
		return err
	}

	return echoContext.JSON(http.StatusCreated, c)
}

func (h *handler) updateInRedis(c *cart) error {
	cartJson, err := json.Marshal(c)
	if err != nil {
		return err
	}
	return h.redis.client.Set(h.redis.context, c.Id, cartJson, 0).Err()
}

// Updates an existing entry. Mostly used for changing the product quantity.
// PATCH /cart/:cartId/entry/:entryId
func (h *handler) patchEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("cartId")
	entryId := echoContext.Param("entryId")
	entryIdInt, err := strconv.Atoi(entryId)
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Parameter entry id should be an integer")
	}

	input := &entry{}
	if err := echoContext.Bind(input); err != nil {
		return err
	}
	result, err := h.redis.client.Get(h.redis.context, cartId).Result()
	if err != nil {
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found.", cartId))
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	if err := updateEntry(c, input, entryIdInt); err != nil {
		return echo.NewHTTPError(http.StatusNotFound, err.Error())
	}

	if err = h.updateInRedis(c); err != nil {
		return err
	}

	return echoContext.JSON(http.StatusOK, c)
}

// Deletes an entry by id.
// DELETE /cart/:cartId/entry/:entryId
func (h *handler) deleteEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("cartId")
	entryId := echoContext.Param("entryId")
	entryIdInt, err := strconv.Atoi(entryId)
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Parameter entry id should be an integer")
	}

	result, err := h.redis.client.Get(h.redis.context, cartId).Result()
	if err != nil {
		return err
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	if err := deleteEntryById(c, entryIdInt); err != nil {
		return echo.NewHTTPError(http.StatusNotFound, err.Error())
	}

	if err = h.updateInRedis(c); err != nil {
		return err
	}

	return echoContext.JSON(http.StatusOK, c)
}

func deleteEntryById(c *cart, id int) error {
	e, err := findEntry(c, id)
	if err != nil {
		return err
	}

	c.Entries = append(c.Entries[:e.Id], c.Entries[e.Id+1:]...)
	return nil
}

func updateEntry(c *cart, input *entry, id int) error {
	e, err := findEntry(c, id)
	if err != nil {
		return err
	}

	e.Quantity = input.Quantity
	return nil
}

func findEntry(c *cart, id int) (*entry, error) {
	for i := range c.Entries {
		if c.Entries[i].Id == id {
			return &c.Entries[i], nil
		}
	}
	return nil, errors.New(fmt.Sprintf("Entry with id [%v] not found.", id))
}

func appendNewEntryToCart(c *cart, input *entry) {
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
