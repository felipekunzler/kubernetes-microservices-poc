package com.paulograbin.payment.authorization;

import com.paulograbin.payment.authorization.usecase.AuthorizationRequest;
import com.paulograbin.payment.authorization.usecase.AuthorizationResponse;
import com.paulograbin.payment.authorization.usecase.AuthorizationUseCase;
import com.paulograbin.payment.creditcard.CreditCardRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/authorization")
public class AuthorizationController {

    @Resource
    private AuthorizationRepository repository;

    @Resource
    private CreditCardRepository creditCardRepository;

    @PostMapping
    public AuthorizationResponse tokenize(@RequestBody AuthorizationRequest request) {
        return new AuthorizationUseCase(request, repository, creditCardRepository).execute();
    }

}
