package com.example.easybankproject.services;

import com.example.easybankproject.db.BankAccountRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.User;
import com.example.easybankproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public BankAccount getBalance(String token) {
        String username = jwtUtil.extractUsername(token);
        return bankAccountRepository.findByUser(userRepository.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String createBankAccount(BankAccount bankAccount, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Unauthorized: User not found.");
        }

        bankAccount.setUser(user);
        bankAccountRepository.save(bankAccount);

        return "Bank account created with ID: " + bankAccount.getBankAccountId();
    }
    public void createBankAccount(User user) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(BigDecimal.TEN);
        bankAccountRepository.save(bankAccount);
    }
}