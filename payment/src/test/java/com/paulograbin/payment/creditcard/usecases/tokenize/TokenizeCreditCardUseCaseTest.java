package com.paulograbin.payment.creditcard.usecases.tokenize;

import com.paulograbin.payment.creditcard.CreditCard;
import com.paulograbin.payment.creditcard.CreditCardRepository;
import com.paulograbin.payment.creditcard.InMemoryCreditCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TokenizeCreditCardUseCaseTest {

    private TokenizeCreditCardUseCase useCase;
    private TokenizeCardRequest request;
    private CreditCardRepository repository;

    @BeforeEach
    void setUp() {
        request = new TokenizeCardRequest();
        repository = new InMemoryCreditCardRepository();

        useCase = new TokenizeCreditCardUseCase(request, repository);
    }

    @Test
    void name() {
        String cardNumber = "4242424242424242";
        CreditCard firstCard = new CreditCard();
        firstCard.setNumber(cardNumber);

        repository.save(firstCard);

        request.number = cardNumber;
        request.cvv = "123";
        request.expirationDate = "1235";


        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isFalse();
        assertThat(execute.invalidExpirationDate).isFalse();
        assertThat(execute.creditCardNumberAlreadyUsed).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name1() {
        request.number = "";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isTrue();
        assertThat(execute.invalidCvv).isTrue();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name2() {
        request.number = "1313";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isTrue();
        assertThat(execute.invalidCvv).isTrue();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name3() {
        request.number = "4242424242424242";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isTrue();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name4() {
        request.number = "4242424242424242";
        request.cvv = "";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isTrue();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name5() {
        request.number = "4242424242424242";
        request.cvv = "1234";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isTrue();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name6() {
        request.number = "4242424242424242";
        request.cvv = "123";
        request.expirationDate = "";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isFalse();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name7() {
        request.number = "4242424242424242";
        request.cvv = "123";
        request.expirationDate = "12345";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isFalse();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isFalse();
        assertThat(execute.invalidExpirationDate).isTrue();

        assertThat(execute.token).isNull();
        assertThat(execute.encoded).isNull();
    }

    @Test
    void name8() {
        request.number = "4242424242424242";
        request.cvv = "123";
        request.expirationDate = "1235";

        TokenizeCardResponse execute = useCase.execute();

        assertThat(execute.successful).isTrue();
        assertThat(execute.invalidNumber).isFalse();
        assertThat(execute.invalidCvv).isFalse();
        assertThat(execute.invalidExpirationDate).isFalse();

        assertThat(execute.token).isNotNull();
        assertThat(execute.encoded).isEqualTo("NDI0MjQyNDI0MjQyNDI0MjEyMzEyMzU=");
    }
}