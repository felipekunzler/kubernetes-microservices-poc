package main

import (
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	e := echo.New()

	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.CORS())

	h := &PaymentHandler{}

	e.POST("/payment/tokenize", h.Tokenize)
	e.POST("/payment/authorize", h.Authorize)
	e.POST("/payment/capture", h.Capture)

	e.Logger.Fatal(e.Start(":1323"))
}
