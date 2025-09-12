package com.paulograbin.payment.authorization.usecase;

import com.paulograbin.payment.authorization.AuthorizationRepository;
import com.paulograbin.payment.authorization.InMemoryAuthorizationRepository;
import com.paulograbin.payment.creditcard.CreditCard;
import com.paulograbin.payment.creditcard.CreditCardRepository;
import com.paulograbin.payment.creditcard.InMemoryCreditCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AuthorizationUseCaseTest {

    private AuthorizationUseCase useCase;
    private AuthorizationRequest request;

    private AuthorizationRepository authorizationRepository;
    private CreditCardRepository creditCardRepository;

    @BeforeEach
    void setUp() {
        request = new AuthorizationRequest();
        authorizationRepository = new InMemoryAuthorizationRepository();
        creditCardRepository = new InMemoryCreditCardRepository();

        useCase = new AuthorizationUseCase(request, authorizationRepository, creditCardRepository);
    }

    @Test
    @DisplayName("Request is not ok")
    void name1() {
        AuthorizationResponse response = useCase.execute();

        assertThat(response.successful).isFalse();
        assertThat(response.authorizationToken).isNull();

        assertThat(response.invalidToken).isTrue();
        assertThat(response.invalidAmount).isTrue();
    }

    @Test
    @DisplayName("Token does not exist, amount is invalid")
    void name11() {
        request.setCreditCardToken("aaa");

        AuthorizationResponse response = useCase.execute();

        assertThat(response.successful).isFalse();
        assertThat(response.authorizationToken).isNull();

        assertThat(response.invalidToken).isFalse();
        assertThat(response.invalidAmount).isTrue();
    }

    @Test
    @DisplayName("Token does not exist, amount is invalid 2")
    void name2() {
        request.creditCardToken = "aaa";
        request.amount = "xx.00";

        AuthorizationResponse response = useCase.execute();

        assertThat(response.successful).isFalse();
        assertThat(response.authorizationToken).isNull();

        assertThat(response.invalidToken).isFalse();
        assertThat(response.invalidAmount).isTrue();
    }

    @Test
    @DisplayName("Request is ok, but credit card token does not exist")
    void name22() {
        request.creditCardToken = "aaa";
        request.amount = "333.00";

        AuthorizationResponse response = useCase.execute();

        assertThat(response.successful).isFalse();
        assertThat(response.authorizationToken).isNull();

        assertThat(response.invalidToken).isFalse();
        assertThat(response.tokenNotFound).isTrue();
        assertThat(response.invalidAmount).isFalse();
    }

    @Test
    @DisplayName("Request is ok, authorization is created")
    void name3() {
        CreditCard c = new CreditCard();
        c.setToken("aaa");
        creditCardRepository.save(c);

        request.creditCardToken = "aaa";
        request.amount = "333.00";

        AuthorizationResponse response = useCase.execute();

        assertThat(response.successful).isTrue();
        assertThat(response.authorizationToken).isNotNull();

        assertThat(response.invalidToken).isFalse();
        assertThat(response.invalidAmount).isFalse();
    }
}