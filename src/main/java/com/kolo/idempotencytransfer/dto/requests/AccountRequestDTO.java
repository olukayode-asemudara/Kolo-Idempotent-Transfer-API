package com.kolo.idempotencytransfer.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AccountRequestDTO {

    public AccountRequestDTO() {
    }

    public AccountRequestDTO(String ownerName, BigDecimal balance) {
        this.ownerName = ownerName;
        this.balance = balance;
    }

    @NotBlank
    private String ownerName;

    @NotNull
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    private BigDecimal balance;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
