package com.paulograbin.payment.capture;

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
public class Capture extends AbstracEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CaptureSequence")
    @SequenceGenerator(name = "CaptureSequence", sequenceName = "CAPTURE_SEQUENCE", allocationSize = 1)
    private Long id;

    private String authorizationToken;
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private String token;

    public Capture() {
        super(null);
    }
}
