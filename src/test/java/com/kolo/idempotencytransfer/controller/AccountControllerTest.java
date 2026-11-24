package com.kolo.idempotencytransfer.controller;

import com.kolo.idempotencytransfer.dto.responses.AccountResponseDTO;
import com.kolo.idempotencytransfer.exception.AccountNotFoundException;
import com.kolo.idempotencytransfer.service.AccountService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    public void shouldCreateAccountSuccessfully() throws Exception {
        AccountResponseDTO response = new AccountResponseDTO(
                1L,
                "Jason Bourne",
                new BigDecimal("5000.00")
        );

        when(accountService.createAccount(any())).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "ownerName": "Jason Bourne",
                                    "balance": 5000.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerName").value("Jason Bourne"))
                .andExpect(jsonPath("$.balance").value(5000.00));
    }

    @Test
    public void shouldRetrieveAccountSuccessfully() throws Exception {
        AccountResponseDTO response = new AccountResponseDTO(
                1L,
                "Jason Bourne",
                new BigDecimal("5000.00")
        );

        when(accountService.getAccountById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ownerName").value("Jason Bourne"))
                .andExpect(jsonPath("$.balance").value(5000.00));
    }

    @Test
    public void shouldRejectAccountWithBlankOwnerName() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "ownerName": "",
                                "balance": 5000.00
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectAccountWithNegativeBalance() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "ownerName": "Jason Bourne",
                                "balance": -100.00
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
        when(accountService.getAccountById(99L))
                .thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/99"))
                .andExpect(status().isNotFound());
    }
}
