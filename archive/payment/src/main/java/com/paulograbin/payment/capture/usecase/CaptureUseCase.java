package com.paulograbin.payment.capture.usecase;

import com.paulograbin.payment.authorization.Authorization;
import com.paulograbin.payment.authorization.AuthorizationRepository;
import com.paulograbin.payment.capture.Capture;
import com.paulograbin.payment.capture.CaptureRepository;
import com.paulograbin.payment.domain.AbstractUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;


public class CaptureUseCase extends AbstractUseCase<CaptureRequest, CaptureResponse> {

    private static final Logger logger = LoggerFactory.getLogger(CaptureUseCase.class);
    private final CaptureRepository repository;
    private final AuthorizationRepository authorizationRepository;

    public CaptureUseCase(CaptureRequest request, CaptureRepository repository, AuthorizationRepository authorizationRepository) {
        this.response = new CaptureResponse();
        this.request = request;
        this.repository = repository;
        this.authorizationRepository = authorizationRepository;
    }

    @Override
    protected void doIt() {
        Capture newCapture = new Capture();
        newCapture.setCreationDate(LocalDateTime.now());
        newCapture.setAmount(new BigDecimal(request.getAmount()));
        newCapture.setAuthorizationToken(request.getAuthorizationToken());
        newCapture.setToken(UUID.randomUUID().toString());

        Capture savedCapture = repository.save(newCapture);

        response.successful = true;
        response.amountCaptured = newCapture.getAmount().toString();
        response.captureToken = newCapture.getToken();
    }

    @Override
    protected boolean isValid() {
        if (isBlank(request.authorizationToken)) {
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


        Optional<Authorization> byToken = authorizationRepository.findByToken(request.getAuthorizationToken());
        if (byToken.isEmpty()) {
            return false;
        }

        Authorization authorization = byToken.get();
        BigDecimal requestedAmount = new BigDecimal(request.getAmount());
        BigDecimal authorizedAmount = authorization.getAmount();

        int i = requestedAmount.compareTo(authorizedAmount);
        if (i > 0) {
            return false;
        }

        return true;
    }

    @Override
    protected void setErrors() {
        if (isBlank(request.authorizationToken)) {
            response.invalidAuthorizationToken = false;
        }

        if (isBlank(request.amount)) {
            response.invalidAmount = false;
        }

        try {
            new BigDecimal(request.amount);
        } catch (NumberFormatException e) {
            response.invalidAmount = true;
            return;
        }

        Optional<Authorization> byToken = authorizationRepository.findByToken(request.getAuthorizationToken());
        if (byToken.isEmpty()) {
            response.invalidAuthorizationToken = true;
            return;
        }

        Authorization authorization = byToken.get();
        BigDecimal requestedAmount = new BigDecimal(request.amount);
        BigDecimal authorizedAmount = authorization.getAmount();

        int i = requestedAmount.compareTo(authorizedAmount);
        if (i > 0) {
            response.amountGreaterThanAuthorized = true;
        }

    }
}
