package com.paulograbin.payment.authorization;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface AuthorizationRepository extends CrudRepository<Authorization, Long> {

    Optional<Authorization> findByToken(String authorizationToken);
}
