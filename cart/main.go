package main

import (
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	initialize()

	e := echo.New()

	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	e.GET("/cart/:id", listCart)
	e.GET("/entry", postEntry)
	//e.DELETE("/entry", deleteEntry)

	e.Logger.Fatal(e.Start(":1323"))
}
