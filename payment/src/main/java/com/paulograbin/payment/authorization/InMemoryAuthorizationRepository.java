package com.paulograbin.payment.authorization;

import com.paulograbin.payment.persistence.InMemoryRepository;

import java.util.Collection;
import java.util.Optional;


public class InMemoryAuthorizationRepository extends InMemoryRepository<Authorization> implements AuthorizationRepository {

    @Override
    public Optional<Authorization> findByToken(String authorizationToken) {
        Collection<Authorization> values = map.values();

        for (Authorization value : values) {
            if (value.getToken().equalsIgnoreCase(authorizationToken)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
