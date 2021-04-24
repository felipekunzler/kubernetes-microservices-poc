package com.paulograbin.payment.creditcard.usecases.tokenize;

import com.paulograbin.payment.creditcard.CreditCard;
import com.paulograbin.payment.creditcard.CreditCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;


public class TokenizeCreditCardUseCase {

    private static final Logger logger = LoggerFactory.getLogger(TokenizeCreditCardUseCase.class);

    private final TokenizeCardRequest request;
    private final TokenizeCardResponse response;
    private final CreditCardRepository repository;


    public TokenizeCreditCardUseCase(TokenizeCardRequest request, CreditCardRepository repository) {
        this.response = new TokenizeCardResponse();
        this.request = request;
        this.repository = repository;
    }

    public TokenizeCardResponse execute() {
        logger.info("Executing use case with request {}", request);

        if (isValid()) {
            tokenizeNewCard();
        } else {
            setErrors();
        }

        logger.info("Returning response {}", response);
        return response;
    }

    private void tokenizeNewCard() {
        String cardInformation = request.number + request.cvv + request.expirationDate;
        byte[] encode = Base64.getEncoder().encode(cardInformation.getBytes(StandardCharsets.UTF_8));

        String encodedString = new String(encode);
        UUID code = UUID.randomUUID();

        logger.info("Creating new cc with encoded {} and token {}", encodedString, code);

        CreditCard newCreditCard = new CreditCard();

        newCreditCard.setNumber(request.number);
        newCreditCard.setCvv(request.cvv);
        newCreditCard.setExpirationDate(request.expirationDate);
        newCreditCard.setEncodedInfo(encodedString);
        newCreditCard.setToken(code.toString());
        newCreditCard.setCreationDate(LocalDateTime.now());

        repository.save(newCreditCard);

        response.successful = true;
        response.token = newCreditCard.getToken();
        response.encoded = newCreditCard.getEncodedInfo();
    }

    private boolean isValid() {
       if (isBlank(request.number)) {
           return false;
       }

       if (request.number.length() != 16) {
           return false;
       }

       if (isBlank(request.cvv)) {
           return false;
       }

        if (request.cvv.length() != 3) {
            return false;
        }

       if (isBlank(request.expirationDate)) {
           return false;
       }

        if (request.expirationDate.length() != 4) {
            return false;
        }

        if (repository.existsByNumber(request.number)) {
            return false;
        }

        return true;
    }

    private void setErrors() {
        if (isBlank(request.number)) {
            response.successful = false;
            response.invalidNumber = true;
        }

        if (request.number.length() != 16) {
            response.successful = false;
            response.invalidNumber = true;
        }

        if (isBlank(request.cvv)) {
            response.successful = false;
            response.invalidCvv = true;
        }

        if (request.cvv.length() != 3) {
            response.successful = false;
            response.invalidCvv = true;
        }

        if (isBlank(request.expirationDate)) {
            response.successful = false;
            response.invalidExpirationDate = true;
        }

        if (request.expirationDate.length() != 4) {
            response.successful = false;
            response.invalidExpirationDate = true;
        }


        if (repository.existsByNumber(request.number)) {
            response.creditCardNumberAlreadyUsed = true;
            response.successful = false;
        }
    }
}
