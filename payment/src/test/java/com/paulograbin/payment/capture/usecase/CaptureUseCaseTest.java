package com.paulograbin.payment.capture.usecase;

import com.paulograbin.payment.authorization.Authorization;
import com.paulograbin.payment.authorization.AuthorizationRepository;
import com.paulograbin.payment.authorization.InMemoryAuthorizationRepository;
import com.paulograbin.payment.capture.CaptureRepository;
import com.paulograbin.payment.capture.InMemoryCaptureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CaptureUseCaseTest {

    private CaptureUseCase useCase;
    private CaptureRequest request;
    private CaptureRepository repository;
    private AuthorizationRepository authorizationRepository;

    @BeforeEach
    void setUp() {
        request = new CaptureRequest();
        repository = new InMemoryCaptureRepository();
        authorizationRepository = new InMemoryAuthorizationRepository();

        useCase = new CaptureUseCase(request, repository, authorizationRepository);
    }

    @Test
    void name() {
        CaptureResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.captureToken).isNull();
        assertThat(execute.amountCaptured).isNull();
        assertThat(execute.tokenNotFound).isFalse();
    }

    @Test
    void name0() {
        request.authorizationToken = "aaaa";
        CaptureResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.captureToken).isNull();
        assertThat(execute.amountCaptured).isNull();
        assertThat(execute.tokenNotFound).isFalse();
    }

    @Test
    void name1() {
        UUID uuid = UUID.randomUUID();

        request.amount = "200.33";
        request.authorizationToken = uuid.toString();

        CaptureResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.captureToken).isNull();
        assertThat(execute.amountCaptured).isNull();
        assertThat(execute.tokenNotFound).isFalse();
    }

    @Test
    void name2() {
        request.authorizationToken = "aaaa";
        request.amount = "aaa.33";

        CaptureResponse execute = useCase.execute();
    }

    @Test
    void name3() {
        Authorization authorization = new Authorization();
        authorization.setToken(UUID.randomUUID().toString());
        authorization.setCreditCardToken("aaaa");
        authorizationRepository.save(authorization);

        request.authorizationToken = authorization.getToken();
        request.amount = "aaa.33";

        CaptureResponse execute = useCase.execute();
        assertThat(execute.successful).isFalse();
    }

    @Test
    void name4() {
        Authorization authorization = new Authorization();
        authorization.setToken(UUID.randomUUID().toString());
        authorization.setAmount(BigDecimal.valueOf(1000));
        authorization.setCreditCardToken("aaaa");
        authorizationRepository.save(authorization);

        request.authorizationToken = authorization.toString();
        request.amount = "1000.00";

        CaptureResponse execute = useCase.execute();
        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidAuthorizationToken).isTrue();
    }

    @Test
    void name5() {
        Authorization authorization = new Authorization();
        authorization.setToken(UUID.randomUUID().toString());
        authorization.setAmount(BigDecimal.valueOf(1500));
        authorization.setCreditCardToken("aaaa");
        authorizationRepository.save(authorization);

        request.authorizationToken = authorization.getToken();
        request.amount = "1000.00";

        CaptureResponse execute = useCase.execute();
        assertThat(execute.successful).isTrue();
    }

    @Test
    void name6() {
        Authorization authorization = new Authorization();
        authorization.setToken(UUID.randomUUID().toString());
        authorization.setAmount(BigDecimal.valueOf(700));
        authorization.setCreditCardToken("aaaa");
        authorizationRepository.save(authorization);

        request.authorizationToken = authorization.getToken();
        request.amount = "1000.00";

        CaptureResponse execute = useCase.execute();
        assertThat(execute.successful).isFalse();
    }
}