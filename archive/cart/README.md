## Cart Service
* Run Redis `docker run --name redis -p 6379:6379 -d redis`
* Run locally with  `go run .`

### Endpoints
* POST http://localhost:1323/cart
* GET http://localhost:1323/cart/:id
* POST http://localhost:1323/cart/:id/entry
* PATCH http://localhost:1323/cart/:cartId/entry/:entryId
* DELETE http://localhost:1323/cart/:cartId/entry/:entryId
