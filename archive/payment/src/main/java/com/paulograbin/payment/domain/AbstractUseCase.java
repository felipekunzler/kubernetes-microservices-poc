package com.paulograbin.payment.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUseCase<R, Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUseCase.class);

    protected R request;
    protected Q response;

    public Q execute() {
        logger.info("Executing with request {}", request);

        if (isValid()) {
            doIt();
        } else {
            setErrors();
        }

        logger.info("Returning response {}", response);
        return response;
    }

    protected abstract void doIt();

    protected abstract void setErrors();

    protected abstract boolean isValid();

}
