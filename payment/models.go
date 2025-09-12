package main

type TokenizeRequest struct {
	CardNumber     string `json:"cardNumber"`
	CardExpiration string `json:"cardExpiration"`
	CVV            string `json:"cvv"`
	CardHolder     string `json:"cardHolder"`
}

type TokenizeResponse struct {
	Token string `json:"token"`
}

type AuthorizeRequest struct {
	Amount    int    `json:"amount"`
	CardToken string `json:"cardToken"`
}

type AuthorizeResponse struct {
	AuthorizationCode string `json:"authorizationCode"`
	Success           bool   `json:"success"`
}

type CaptureRequest struct {
	Amount            int    `json:"amount"`
	AuthorizationCode string `json:"authorizationCode"`
}

type CaptureResponse struct {
	CaptureID string `json:"captureId"`
	Success   bool   `json:"success"`
}
