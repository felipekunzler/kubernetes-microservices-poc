package com.paulograbin.payment.creditcard;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

    boolean existsByNumber(String number);

    Optional<CreditCard> findByToken(String token);

}
