package com.kolo.idempotencytransfer.service;

import com.kolo.idempotencytransfer.data.models.Account;
import com.kolo.idempotencytransfer.data.repositories.AccountRepository;
import com.kolo.idempotencytransfer.dto.requests.AccountRequestDTO;
import com.kolo.idempotencytransfer.dto.responses.AccountResponseDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void shouldRetrieveAccountSuccessfully() {
        Account account = new Account(
                "Jason Bourne",
                new BigDecimal("5000.00")
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountResponseDTO response = accountService.getAccountById(1L);

        assertNotNull(response);
        assertEquals("Jason Bourne", response.getOwnerName());
        assertEquals(new BigDecimal("5000.00"), response.getBalance());

        verify(accountRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> accountService.getAccountById(1L)
        );

        verify(accountRepository).findById(1L);
    }

    @Test
    public void shouldSaveAccountWithCorrectDetails() {
        AccountRequestDTO request = new AccountRequestDTO(
                "Jason Bourne",
                new BigDecimal("5000.00")
        );

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        accountService.createAccount(request);

        ArgumentCaptor<Account> accountCaptor =
                ArgumentCaptor.forClass(Account.class);

        verify(accountRepository).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();

        assertEquals("Jason Bourne", capturedAccount.getOwnerName());
        assertEquals(new BigDecimal("5000.00"), capturedAccount.getBalance());
    }

    @Test
    void shouldRejectBlankOwnerName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        AccountRequestDTO request = new AccountRequestDTO(
                "",
                new BigDecimal("5000.00")
        );

        assertFalse(validator.validate(request).isEmpty());

        factory.close();
    }

    @Test
    void shouldRejectNullOwnerName() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        AccountRequestDTO request = new AccountRequestDTO(
                null,
                new BigDecimal("5000.00")
        );

        assertFalse(validator.validate(request).isEmpty());

        factory.close();
    }

    @Test
    void shouldRejectNullBalance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        AccountRequestDTO request = new AccountRequestDTO(
                "Jason Bourne",
                null
        );

        assertFalse(validator.validate(request).isEmpty());

        factory.close();
    }

    @Test
    void shouldRejectNegativeBalance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        AccountRequestDTO request = new AccountRequestDTO(
                "Jason Bourne",
                new BigDecimal("-100.00")
        );

        assertFalse(validator.validate(request).isEmpty());

        factory.close();
    }

    @Test
    void shouldAcceptZeroBalance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        AccountRequestDTO request = new AccountRequestDTO(
                "Jason Bourne",
                new BigDecimal("0.00")
        );

        assertTrue(validator.validate(request).isEmpty());

        factory.close();
    }
}
