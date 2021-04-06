package main

import (
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	e := echo.New()

	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	r := NewRedisStore()
	h := handler{redis: r}

	e.GET("/cart/:id", h.listCart)
	e.POST("/cart", h.createCart)
	e.POST("/cart/:id/entry", h.postEntry)
	e.PATCH("/cart/:cartId/entry/:entryId", h.patchEntry)
	e.DELETE("/cart/:cartId/entry/:entryId", h.deleteEntry)

	e.Logger.Fatal(e.Start(":1323"))
}
