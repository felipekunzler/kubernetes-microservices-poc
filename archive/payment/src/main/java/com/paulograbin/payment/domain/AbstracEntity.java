package com.paulograbin.payment.domain;

public abstract class AbstracEntity {

    protected Long id;

    public AbstracEntity(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
