package com.paulograbin.payment.creditcard;

import com.paulograbin.payment.persistence.InMemoryRepository;

import java.util.Collection;
import java.util.Optional;


public class InMemoryCreditCardRepository extends InMemoryRepository<CreditCard> implements CreditCardRepository {

    @Override
    public boolean existsByNumber(String number) {
        Collection<CreditCard> values = map.values();

        for (CreditCard value : values) {
            if (value.getNumber().equalsIgnoreCase(number)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Optional<CreditCard> findByToken(String token) {
        Collection<CreditCard> values = map.values();

        for (CreditCard value : values) {
            if (value.getToken().equalsIgnoreCase(token)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
