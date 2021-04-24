package com.paulograbin.payment.creditcard;

import com.paulograbin.payment.domain.AbstracEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"number"})
})
public class CreditCard extends AbstracEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CreditCardSequence")
    @SequenceGenerator(name = "CreditCardSequence", sequenceName = "CREDIT_CARD_SEQUENCE", allocationSize = 1)
    private Long id;

    private LocalDateTime creationDate;
    private String number;
    private String expirationDate;
    private String cvv;

    private String token;
    private String encodedInfo;

    public CreditCard() {
        super(null);
    }
}
