## Payment Service
* Run locally with  `go run .`

### Endpoints
* POST http://localhost:1323/payment/tokenize
Sample request:
```
{
    "cardNumber": "4242424242424242",
    "cardExpiration": "12/2028",
    "cvv": "123",
    "cardHolder": "First Last"
}
```
Sample response:
```
{
    "token": "ABDC1234EFGH5678"
}
```
* POST http://localhost:1323/payment/authorize
Sample request:
```
{
    "amount": 1000,
    "cardToken": "ABDC1234EFGH5678"
}
```
Sample response:
```
{
    "authorizationCode": "123456",
    "success": true
}
```
* POST http://localhost:1323/payment/capture
  Sample request:
```
{
    "amount": 1000,
    "authorizationCode": "123456"
}
```
Sample response:
```
{
    "captureId": "122111",
    "success": true
}
```
