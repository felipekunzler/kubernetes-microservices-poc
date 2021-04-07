FROM golang:1.14

WORKDIR /go/src/cart
COPY . .

RUN go get -d -v ./...
RUN go install -v ./...

CMD ["cart"]
