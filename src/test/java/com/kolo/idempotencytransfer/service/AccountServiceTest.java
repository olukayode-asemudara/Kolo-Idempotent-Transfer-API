package com.kolo.idempotencytransfer.service;

import com.kolo.idempotencytransfer.data.models.Account;
import com.kolo.idempotencytransfer.data.repositories.AccountRepository;
import com.kolo.idempotencytransfer.dto.requests.AccountRequestDTO;
import com.kolo.idempotencytransfer.dto.responses.AccountResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccountSuccessfully() {
        AccountRequestDTO request = new AccountRequestDTO(
                "Jason Bourne",
                new BigDecimal("5000.00")
        );

        Account savedAccount = new Account(
                "Jason Bourne",
                new BigDecimal("5000.00")
        );

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        AccountResponseDTO response = accountService.createAccount(request);
        assertNotNull(response);
        assertEquals("Jason Bourne", response.getOwnerName());
        assertEquals(new BigDecimal("5000.00"), response.getBalance());
        verify(accountRepository).save(any(Account.class));
    }
}
