package com.kolo.idempotencytransfer.service;

import com.kolo.idempotencytransfer.data.models.Account;
import com.kolo.idempotencytransfer.data.models.Transfer;
import com.kolo.idempotencytransfer.data.repositories.AccountRepository;
import com.kolo.idempotencytransfer.data.repositories.TransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Mock
    private TransferRepository transferRepository;

    @Test
    public void shouldTransferMoneySuccessfully() {
        Account sender = new Account(
                "Alice",
                new BigDecimal("5000.00")
        );

        Account recipient = new Account(
                "Bob",
                new BigDecimal("2000.00")
        );

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(recipient));

        transferService.transfer(1L, 2L, new BigDecimal("1000.00"));

        assertEquals(new BigDecimal("4000.00"), sender.getBalance());
        assertEquals(new BigDecimal("3000.00"), recipient.getBalance());

        verify(accountRepository).save(sender);
        verify(accountRepository).save(recipient);
        verify(transferRepository).save(any(Transfer.class));
    };

    @Test
    void shouldRejectTransferToSameAccount() {
        Account account = new Account(
                "Alice",
                new BigDecimal("5000.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(
                        1L,
                        1L,
                        new BigDecimal("1000.00")
                )
        );

        verify(accountRepository, never()).save(any(Account.class));
        verify(transferRepository, never()).save(any(Transfer.class));
    }
}
