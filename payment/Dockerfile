FROM golang:1.25

WORKDIR /go/src/payment
COPY . .

RUN go get -d -v ./...
RUN go install -v ./...

CMD ["payment"]
