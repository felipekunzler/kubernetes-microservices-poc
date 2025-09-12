package com.paulograbin.payment.capture.usecase;

import lombok.Data;


@Data
public class CaptureRequest {

    String authorizationToken = "";
    String amount = "";

}
