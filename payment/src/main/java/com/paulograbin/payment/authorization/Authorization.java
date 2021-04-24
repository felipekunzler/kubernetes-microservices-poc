package com.paulograbin.payment.authorization;

import com.paulograbin.payment.domain.AbstracEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class Authorization extends AbstracEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AuthorizationSequence")
    @SequenceGenerator(name = "AuthorizationSequence", sequenceName = "AUTHORIZATION_SEQUENCE", allocationSize = 1)
    private Long id;

    private String creditCardToken;
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private String token;

    public Authorization() {
        super(null);
    }

}
