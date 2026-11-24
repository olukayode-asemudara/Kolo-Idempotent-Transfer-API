package com.kolo.idempotencytransfer.service;

import com.kolo.idempotencytransfer.data.models.Account;
import com.kolo.idempotencytransfer.data.repositories.AccountRepository;
import com.kolo.idempotencytransfer.dto.requests.AccountRequestDTO;
import com.kolo.idempotencytransfer.dto.responses.AccountResponseDTO;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Account account = new Account(
                request.getOwnerName(),
                request.getBalance()
        );

        Account savedAccount = accountRepository.save(account);

        return new AccountResponseDTO(
                savedAccount.getId(),
                savedAccount.getOwnerName(),
                savedAccount.getBalance()
        );
    }

    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new AccountResponseDTO(
                account.getId(),
                account.getOwnerName(),
                account.getBalance()
        );
    }
}
