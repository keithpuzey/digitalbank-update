package io.digisic.bank.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "transaction_number_seq")
public class TransactionNumberSeq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    public Long getId() {
        return id;
    }
}