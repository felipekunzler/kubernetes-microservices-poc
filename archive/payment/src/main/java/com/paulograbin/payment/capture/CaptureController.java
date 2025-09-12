package com.paulograbin.payment.capture;

import com.paulograbin.payment.authorization.AuthorizationRepository;
import com.paulograbin.payment.capture.usecase.CaptureResponse;
import com.paulograbin.payment.capture.usecase.CaptureUseCase;
import com.paulograbin.payment.creditcard.usecases.tokenize.TokenizeCardRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/capture")
public class CaptureController {

    @Resource
    private CaptureRepository repository;

    @Resource
    private AuthorizationRepository authorizationRepository;

    @PostMapping
    public CaptureResponse tokenize(@RequestBody TokenizeCardRequest request) {
        return new CaptureUseCase(request, repository, authorizationRepository).execute();
    }

}
