package main

import (
	"github.com/labstack/echo/v4"
	"net/http"
)

type cart struct {
	Id    string  `json:"id"`
	Total float32 `json:"total"`
}

func listCarts(c echo.Context) error {
	aCart := cart{
		Id:    "a",
		Total: 123,
	}
	return c.JSON(http.StatusOK, aCart)
}
