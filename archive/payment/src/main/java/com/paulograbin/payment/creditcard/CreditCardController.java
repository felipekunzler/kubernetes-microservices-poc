package com.paulograbin.payment.creditcard;

import com.paulograbin.payment.creditcard.usecases.tokenize.TokenizeCardRequest;
import com.paulograbin.payment.creditcard.usecases.tokenize.TokenizeCardResponse;
import com.paulograbin.payment.creditcard.usecases.tokenize.TokenizeCreditCardUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping(value = "/creditcard")
public class CreditCardController {

    @Resource
    private CreditCardRepository repository;

    @PostMapping
    public TokenizeCardResponse tokenize(@RequestBody TokenizeCardRequest request) {
        return new TokenizeCreditCardUseCase(request, repository).execute();
    }

}
