package com.paulograbin.payment.authorization.usecase;

import lombok.Data;


@Data
public class AuthorizationResponse {

    public boolean successful;
    public String authorizationToken;
    public boolean invalidToken;
    public boolean invalidAmount;
    public boolean tokenNotFound;
}
