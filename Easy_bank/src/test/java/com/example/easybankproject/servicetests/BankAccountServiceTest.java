package com.example.easybankproject.servicetests;

import com.example.easybankproject.db.BankAccountRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.User;
import com.example.easybankproject.services.BankAccountService;
import com.example.easybankproject.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BankAccountService bankAccountService;

    private User user;
    private BankAccount bankAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testuser");

        bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(BigDecimal.TEN);
    }

    @Test
    public void testGetBalanceSuccess() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bankAccountRepository.findByUser(user)).thenReturn(Optional.of(bankAccount));

        // Act
        BankAccount result = bankAccountService.getBalance(token);

        // Assert
        assertEquals(bankAccount, result);
    }

    @Test
    public void testGetBalanceUserNotFound() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bankAccountService.getBalance(token), "User not found");
    }

    @Test
    public void testCreateBankAccountWithBankAccountSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        String result = bankAccountService.createBankAccount(bankAccount, "testuser");

        // Assert
        assertEquals("Bank account created with ID: " + bankAccount.getBankAccountId(), result);
    }

    @Test
    public void testCreateBankAccountWithBankAccountUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bankAccountService.createBankAccount(bankAccount, "testuser"), "Unauthorized: User not found.");
    }

    @Test
    public void testCreateBankAccountWithUserSuccess() {
        // Act
        bankAccountService.createBankAccount(user);

        // Assert
        assertEquals(user, bankAccount.getUser());
        assertEquals(BigDecimal.TEN, bankAccount.getBalance());
    }
}