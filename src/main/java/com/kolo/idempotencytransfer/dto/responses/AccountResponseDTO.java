package com.kolo.idempotencytransfer.dto.responses;

import java.math.BigDecimal;

public class AccountResponseDTO {
    private Long id;
    private String ownerName;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountResponseDTO(Long id, String ownerName, BigDecimal balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }
}
