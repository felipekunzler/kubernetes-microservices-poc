package main

import (
	"net/http"
	"strings"

	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
)

type PaymentHandler struct{}

func (h *PaymentHandler) Tokenize(c echo.Context) error {
	var req TokenizeRequest
	if err := bind(c, &req); err != nil {
		return err
	}

	if req.CardNumber == "" {
		return echo.NewHTTPError(http.StatusBadRequest, "card number is required")
	}

	return c.JSON(http.StatusOK, TokenizeResponse{Token: newID(16)})
}

func (h *PaymentHandler) Authorize(c echo.Context) error {
	var req AuthorizeRequest
	if err := bind(c, &req); err != nil {
		return err
	}

	if err := validateAmount(req.Amount); err != nil {
		return err
	}

	return c.JSON(http.StatusOK, AuthorizeResponse{AuthorizationCode: newID(6), Success: true})
}

func (h *PaymentHandler) Capture(c echo.Context) error {
	var req CaptureRequest
	if err := bind(c, &req); err != nil {
		return err
	}

	if err := validateAmount(req.Amount); err != nil {
		return err
	}

	return c.JSON(http.StatusOK, CaptureResponse{CaptureID: newID(6), Success: true})
}

func bind[T any](c echo.Context, req *T) error {
	if err := c.Bind(req); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "invalid request body")
	}
	return nil
}

func newID(length int) string {
	id := strings.ToUpper(strings.ReplaceAll(uuid.New().String(), "-", ""))
	if len(id) < length {
		return id
	}
	return id[:length]
}

func validateAmount(amount int) error {
	if amount < 0 {
		return echo.NewHTTPError(http.StatusBadRequest, "amount cannot be negative")
	}
	return nil
}
