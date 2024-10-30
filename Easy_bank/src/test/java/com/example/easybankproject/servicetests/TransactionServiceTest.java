package com.example.easybankproject.servicetests;

import com.example.easybankproject.db.BankAccountRepository;
import com.example.easybankproject.db.TransactionRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.models.User;
import com.example.easybankproject.services.NotificationService;
import com.example.easybankproject.services.TransactionService;
import com.example.easybankproject.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private BankAccount senderAccount;
    private BankAccount receiverAccount;
    private Transaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testuser");

        senderAccount = new BankAccount();
        senderAccount.setBankAccountId(1);
        senderAccount.setUser(user);
        senderAccount.setBalance(BigDecimal.valueOf(1000));

        receiverAccount = new BankAccount();
        receiverAccount.setBankAccountId(2);
        receiverAccount.setUser(user);
        receiverAccount.setBalance(BigDecimal.valueOf(500));

        transaction = new Transaction();
        transaction.setSenderAccountId(1);
        transaction.setReceiverAccountId(2);
        transaction.setAmount(100);
    }

    @Test
    public void testCreateTransactionSuccess() {
        // Arrange
        when(bankAccountRepository.findByBankAccountId(1)).thenReturn(Optional.of(senderAccount));
        when(bankAccountRepository.findByBankAccountId(2)).thenReturn(Optional.of(receiverAccount));

        // Act
        String result = transactionService.createTransaction(transaction);

        // Assert
        assertEquals("Transaction created with ID: " + transaction.getTransactionId(), result);
        verify(bankAccountRepository, times(1)).save(senderAccount);
        verify(bankAccountRepository, times(1)).save(receiverAccount);
        verify(transactionRepository, times(1)).save(transaction);
        verify(notificationService, times(2)).createNotification(any(User.class), any(Transaction.class), anyString());
    }

    @Test
    public void testCreateTransactionInsufficientFunds() {
        // Arrange
        transaction.setAmount(2000);
        when(bankAccountRepository.findByBankAccountId(1)).thenReturn(Optional.of(senderAccount));
        when(bankAccountRepository.findByBankAccountId(2)).thenReturn(Optional.of(receiverAccount));

        // Act
        String result = transactionService.createTransaction(transaction);

        // Assert
        assertEquals("Insufficient funds.", result);
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(notificationService, never()).createNotification(any(User.class), any(Transaction.class), anyString());
    }

    @Test
    public void testCreateTransactionAccountNotFound() {
        // Arrange
        when(bankAccountRepository.findByBankAccountId(1)).thenReturn(Optional.empty());
        when(bankAccountRepository.findByBankAccountId(2)).thenReturn(Optional.of(receiverAccount));

        // Act
        String result = transactionService.createTransaction(transaction);

        // Assert
        assertEquals("Sender or receiver account not found.", result);
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(notificationService, never()).createNotification(any(User.class), any(Transaction.class), anyString());
    }

    @Test
    public void testGetSenderIdSuccess() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bankAccountRepository.findByUser(user)).thenReturn(Optional.of(senderAccount));

        // Act
        int senderId = transactionService.getSenderId(token);

        // Assert
        assertEquals(1, senderId);
    }

    @Test
    public void testGetSenderIdUserNotFound() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> transactionService.getSenderId(token), "User not found");
    }

    @Test
    public void testGetTransactionsSuccess() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bankAccountRepository.findByUser(user)).thenReturn(Optional.of(senderAccount));
        when(transactionRepository.findAllBySenderAccountIdOrReceiverAccountId(1, 1)).thenReturn(List.of(transaction));

        // Act
        List<Transaction> transactions = transactionService.getTransactions(token);

        // Assert
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    @Test
    public void testGetTransactionsUserNotFound() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> transactionService.getTransactions(token), "User not found");
    }
}