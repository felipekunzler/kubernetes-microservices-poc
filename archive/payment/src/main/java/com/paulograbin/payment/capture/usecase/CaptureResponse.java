package com.paulograbin.payment.capture.usecase;

import lombok.Data;


@Data
public class CaptureResponse {

    public boolean successful;
    public String captureToken;
    public String amountCaptured;

    public boolean invalidAmount;
    public boolean tokenNotFound;
    public boolean invalidAuthorizationToken;
    public boolean amountGreaterThanAuthorized;
}
