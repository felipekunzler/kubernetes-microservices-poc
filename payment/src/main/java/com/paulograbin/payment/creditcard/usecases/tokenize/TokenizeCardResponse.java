package com.paulograbin.payment.creditcard.usecases.tokenize;

import lombok.Data;


@Data
public class TokenizeCardResponse {

    public boolean successful;
    public String token;
    public String encoded;

    public boolean invalidNumber;
    public boolean invalidCvv;
    public boolean invalidExpirationDate;
    public boolean creditCardNumberAlreadyUsed;
}
