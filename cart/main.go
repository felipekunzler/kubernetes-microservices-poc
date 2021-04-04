package main

import (
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	initializeRedis()

	e := echo.New()

	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	e.GET("/cart/:id", listCart)
	e.POST("/cart", createCart)
	e.POST("/cart/:id/entry", postEntry)
	e.PATCH("/cart/:cartId/entry/:entryId", patchEntry)
	e.DELETE("/cart/:cartId/entry/:entryId", deleteEntry)

	e.Logger.Fatal(e.Start(":1323"))
}
