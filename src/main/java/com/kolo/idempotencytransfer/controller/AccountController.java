package com.kolo.idempotencytransfer.controller;

import com.kolo.idempotencytransfer.dto.requests.AccountRequestDTO;
import com.kolo.idempotencytransfer.dto.responses.AccountResponseDTO;
import com.kolo.idempotencytransfer.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(
            @Valid @RequestBody AccountRequestDTO request) {

        AccountResponseDTO response = accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(
            @PathVariable Long id) {

        AccountResponseDTO response = accountService.getAccountById(id);

        return ResponseEntity.ok(response);
    }
}
