package com.kolo.idempotencytransfer.service;

import com.kolo.idempotencytransfer.data.models.Account;
import com.kolo.idempotencytransfer.data.models.Transfer;
import com.kolo.idempotencytransfer.data.repositories.AccountRepository;
import com.kolo.idempotencytransfer.data.repositories.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransferService {
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(
            AccountRepository accountRepository,
            TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        if (amount.compareTo(new BigDecimal("0.00")) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        Account sender = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account recipient = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(recipient);

        Transfer transfer = new Transfer(
                fromAccountId,
                toAccountId,
                amount,
                "SUCCESS",
                LocalDateTime.now()
        );

        transferRepository.save(transfer);
    }
}
