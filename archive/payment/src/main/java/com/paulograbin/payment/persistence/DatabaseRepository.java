package com.paulograbin.payment.persistence;

import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public abstract class DatabaseRepository<T, ID> implements EntityRepository<T, ID> {
}
