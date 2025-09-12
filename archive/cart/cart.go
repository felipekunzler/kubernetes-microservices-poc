package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
	"github.com/nats-io/nats.go"
	"io/ioutil"
	"math"
	"net/http"
	"strconv"
)

type cart struct {
	Id      string  `json:"id"`
	Total   float64 `json:"total"`
	Entries []entry `json:"entries"`
}

type entry struct {
	Id       int     `json:"id"`
	Quantity int     `json:"quantity"`
	Total    float64 `json:"total"`
	Product  product `json:"product"`
}

type product struct {
	Id          string  `json:"id"`
	Name        string  `json:"name"`
	Description string  `json:"description"`
	Image       string  `json:"image"`
	Price       float64 `json:"price"`
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
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found", id))
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

// Creates a new entry in a given cart.
// POST /cart/:id/entry
func (h *handler) postEntry(echoContext echo.Context) error {
	cartId := echoContext.Param("id")

	result, err := h.redis.client.Get(h.redis.context, cartId).Result()
	if err != nil {
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found", cartId))
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	input := &entry{}
	if err := echoContext.Bind(input); err != nil {
		return err
	}

	p, err := fetchProduct(input.Product.Id)
	if err != nil {
		return err
	}
	input.Product = *p

	if err = appendNewEntryToCart(c, input); err != nil {
		return err
	}
	if err = h.updateInRedis(c); err != nil {
		return err
	}

	return echoContext.JSON(http.StatusCreated, c)
}

// Fetch product information from the product service
func fetchProduct(id string) (*product, error) {
	url := productServiceUrl + "/api/product/" + id
	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, echo.NewHTTPError(http.StatusBadRequest, fmt.Sprintf("Product with id [%v] not found", id))
	}

	bytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	var p *product
	if err = json.Unmarshal(bytes, &p); err != nil {
		return nil, err
	}

	return p, nil
}

func (h *handler) updateInRedis(c *cart) error {
	recalculateCart(c)
	cartJson, err := json.Marshal(c)
	if err != nil {
		return err
	}
	return h.redis.client.Set(h.redis.context, c.Id, cartJson, 0).Err()
}

func recalculateCart(c *cart) {
	total := float64(0)
	for i := range c.Entries {
		e := &c.Entries[i]
		e.Total = round(e.Product.Price * float64(e.Quantity))
		total += e.Total
	}
	c.Total = round(total)
}

func round(value float64) float64 {
	return math.Round(value*100) / 100
}

// Places the order by sending a message to RabbitMQ which will be
// consumed by the Order Service.
// POST /cart/:id/placeOrder
func (h *handler) placeOrder(echoContext echo.Context) error {
	id := echoContext.Param("id")
	result, err := h.redis.client.Get(h.redis.context, id).Result()
	if err != nil {
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found", id))
	}

	var c *cart
	err = json.Unmarshal([]byte(result), &c)
	if err != nil {
		return err
	}

	nc, err := nats.Connect(natsServerUrl)
	if err != nil {
		return err
	}

	err = nc.Publish("placeOrder", []byte(result))
	if err != nil {
		return err
	}

	err = nc.Drain()
	if err != nil {
		return err
	}

	return echoContext.JSON(http.StatusOK, c)
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
		return echo.NewHTTPError(http.StatusNotFound, fmt.Sprintf("Cart with id [%v] not found", cartId))
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
	i, _, err := findEntry(c, id)
	if err != nil {
		return err
	}

	c.Entries = append(c.Entries[:i], c.Entries[i+1:]...)
	return nil
}

func updateEntry(c *cart, input *entry, id int) error {
	_, e, err := findEntry(c, id)
	if err != nil {
		return err
	}

	e.Quantity = input.Quantity
	return nil
}

func findEntry(c *cart, id int) (int, *entry, error) {
	for i := range c.Entries {
		if c.Entries[i].Id == id {
			return i, &c.Entries[i], nil
		}
	}
	return -1, nil, errors.New(fmt.Sprintf("Entry with id [%v] not found", id))
}

func appendNewEntryToCart(c *cart, input *entry) error {
	var maxId int
	for _, e := range c.Entries {
		if e.Id > maxId {
			maxId = e.Id
		}
		if e.Product.Id == input.Product.Id {
			return echo.NewHTTPError(http.StatusBadRequest,
				fmt.Sprintf("Product with code [%v] already in entry with id [%v]", e.Product.Id, e.Id))
		}
	}

	c.Entries = append(c.Entries, entry{
		Id:       maxId + 1,
		Quantity: input.Quantity,
		Product:  input.Product,
	})
	return nil
}
