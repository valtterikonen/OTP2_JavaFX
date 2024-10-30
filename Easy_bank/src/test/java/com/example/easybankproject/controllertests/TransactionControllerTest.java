package com.example.easybankproject.controllertests;

import com.example.easybankproject.controllers.TransactionController;
import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setSenderAccountId(1);
        transaction.setReceiverAccountId(2);
        transaction.setAmount(1);
        transaction.setMessage("Test transaction");
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateTransaction_Success() throws Exception {

        when(transactionService.createTransaction(any(Transaction.class)))
                .thenReturn("Transaction created successfully");

        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction))
                        .with(csrf()))  // Add a CSRF token to the request
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction created successfully"));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateTransaction_Failure() throws Exception {
        when(transactionService.createTransaction(any(Transaction.class)))
                .thenReturn("Transaction creation failed");

        mockMvc.perform(post("/api/transaction/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Transaction creation failed"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetTransactions_Success() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        when(transactionService.getTransactions(any(String.class)))
                .thenReturn(transactions);

        mockMvc.perform(get("/api/transaction/history")
                        .header("Authorization", "Bearer some-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactions)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetTransactions_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/transaction/history"))
                .andExpect(status().isBadRequest());
    }
}