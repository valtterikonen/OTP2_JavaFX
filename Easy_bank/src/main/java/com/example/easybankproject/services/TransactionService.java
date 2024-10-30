package com.example.easybankproject.services;

import com.example.easybankproject.db.BankAccountRepository;
import com.example.easybankproject.db.TransactionRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String createTransaction(Transaction transaction) {
        Optional<BankAccount> senderAccount = bankAccountRepository.findByBankAccountId(transaction.getSenderAccountId());
        Optional<BankAccount> receiverAccount = bankAccountRepository.findByBankAccountId(transaction.getReceiverAccountId());

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            return "Sender or receiver account not found.";
        }
        if (senderAccount.get().getBalance().compareTo(BigDecimal.valueOf(transaction.getAmount())) < 0) {
            return "Insufficient funds.";
        }

        senderAccount.get().setBalance(senderAccount.get().getBalance().subtract(BigDecimal.valueOf(transaction.getAmount())));
        receiverAccount.get().setBalance(receiverAccount.get().getBalance().add(BigDecimal.valueOf(transaction.getAmount())));
        bankAccountRepository.save(senderAccount.get());
        bankAccountRepository.save(receiverAccount.get());

        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        notificationService.createNotification(senderAccount.get().getUser(), transaction, "You sent " + transaction.getAmount() + " € to account " + transaction.getReceiverAccountId());
        notificationService.createNotification(receiverAccount.get().getUser(), transaction, "You received " + transaction.getAmount() + " € from account " + transaction.getSenderAccountId());

        return "Transaction created with ID: " + transaction.getTransactionId();
    }
    public int getSenderId(String token) {
        String username = jwtUtil.extractUsername(token);
        BankAccount bankAccount = bankAccountRepository.findByUser(userRepository.findByUsername(username)).orElseThrow(() -> new RuntimeException("User not found"));
        return bankAccount.getBankAccountId();
    }

    public List<Transaction> getTransactions(String token) {
        String username = jwtUtil.extractUsername(token);
        BankAccount bankAccount = bankAccountRepository.findByUser(userRepository.findByUsername(username)).orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findAllBySenderAccountIdOrReceiverAccountId(bankAccount.getBankAccountId(), bankAccount.getBankAccountId());
    }
}
