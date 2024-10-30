

package com.example.easybankproject.controllers;

import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
        String result = transactionService.createTransaction(transaction);
        if (result.startsWith("Transaction created")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }


    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        List<Transaction> transactions = transactionService.getTransactions(jwtToken);
        return ResponseEntity.ok(transactions);
    }
}
