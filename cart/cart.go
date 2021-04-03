package main

import (
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
	Quantity  int    `json:"total"`
}

func listCart(c echo.Context) error {
	id := c.Param("id")
	aCart := cart{
		Id:    id,
		Total: 123,
		Entries: []entry{
			{
				ProductId: "abc",
				Quantity:  2,
			},
		},
	}
	return c.JSON(http.StatusOK, aCart)
}

func postEntry(c echo.Context) error {
	aEntry := entry{
		ProductId: "bcd",
		Quantity:  2,
	}

	var aCart cart
	aCart.Id = "newId"
	aCart.Entries = []entry{aEntry}

	return c.JSON(http.StatusOK, aEntry)
}
