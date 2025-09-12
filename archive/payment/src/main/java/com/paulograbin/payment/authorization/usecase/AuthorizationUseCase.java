package com.paulograbin.payment.authorization.usecase;

import com.paulograbin.payment.authorization.Authorization;
import com.paulograbin.payment.authorization.AuthorizationRepository;
import com.paulograbin.payment.creditcard.CreditCard;
import com.paulograbin.payment.creditcard.CreditCardRepository;
import com.paulograbin.payment.domain.AbstractUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;


public class AuthorizationUseCase extends AbstractUseCase<AuthorizationRequest, AuthorizationResponse> {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationUseCase.class);

    private final CreditCardRepository creditCardRepository;
    private final AuthorizationRepository repository;

    public AuthorizationUseCase(AuthorizationRequest request, AuthorizationRepository authorizationRepository, CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
        this.repository = authorizationRepository;
        this.response = new AuthorizationResponse();
        this.request = request;
    }

    @Override
    protected void doIt() {
        Authorization newAuthorization = new Authorization();
        newAuthorization.setCreationDate(LocalDateTime.now());
        newAuthorization.setAmount(new BigDecimal(request.getAmount()));
        newAuthorization.setCreditCardToken(request.creditCardToken);
        newAuthorization.setToken(UUID.randomUUID().toString());

        repository.save(newAuthorization);

        response.successful = true;
        response.authorizationToken = String.valueOf(newAuthorization.getToken());
    }

    @Override
    protected boolean isValid() {
        if (isBlank(request.creditCardToken)) {
            return false;
        }

        if (isBlank(request.amount)) {
            return false;
        }

        try {
            new BigDecimal(request.amount);
        } catch (NumberFormatException e) {
            return false;
        }

        Optional<CreditCard> byToken = creditCardRepository.findByToken(request.creditCardToken);
        if (byToken.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    protected void setErrors() {
        if (isBlank(request.creditCardToken)) {
            response.invalidToken = true;
        }

        if (isBlank(request.amount)) {
            response.invalidAmount = true;
        }

        try {
            new BigDecimal(request.amount);
        } catch (NumberFormatException e) {
            response.invalidAmount = true;
        }

        Optional<CreditCard> byToken = creditCardRepository.findByToken(request.creditCardToken);
        if (byToken.isEmpty()) {
            response.tokenNotFound = true;
        }
    }
}
