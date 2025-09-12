package com.paulograbin.payment.creditcard.usecases.tokenize;

import com.paulograbin.payment.capture.usecase.CaptureRequest;
import lombok.Data;


@Data
public class TokenizeCardRequest extends CaptureRequest {

    public String number = "";
    public String expirationDate = "";
    public String cvv = "";

}
