package com.paulograbin.payment.authorization.usecase;

import lombok.Data;


@Data
public class AuthorizationRequest {

    String amount = "";
    String creditCardToken = "";

}
