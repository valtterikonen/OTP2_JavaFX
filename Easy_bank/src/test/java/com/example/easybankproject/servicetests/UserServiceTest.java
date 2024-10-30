package com.example.easybankproject.servicetests;

import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.User;
import com.example.easybankproject.services.BankAccountService;
import com.example.easybankproject.services.NotificationService;
import com.example.easybankproject.services.UserService;
import com.example.easybankproject.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private NotificationService notificationService;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testGetUserData() {
        // Arrange
        String token = "validToken";
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        User result = userService.getUserData(token);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testUpdateUserData() {
        // Arrange
        String token = "validToken";
        User updatedUser = new User();
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setAddress("New Address");
        updatedUser.setPhonenumber(1234567890);
        updatedUser.setFirstname("NewFirstName");
        updatedUser.setLastname("NewLastName");

        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        User result = userService.updateUserData(token, updatedUser);

        // Assert
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("New Address", result.getAddress());
        assertEquals(1234567890, result.getPhonenumber());
        assertEquals("NewFirstName", result.getFirstname());
        assertEquals("NewLastName", result.getLastname());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtUtil.generateToken("testuser")).thenReturn("generatedToken");

        // Act
        String result = userService.registerUser(user);

        // Assert
        assertEquals("generatedToken", result);
        verify(userRepository, times(1)).save(user);
        verify(bankAccountService, times(1)).createBankAccount(user);
        verify(notificationService, times(1)).createNotification(user, null, "testuser has registered successfully.");
    }

    @Test
    public void testRegisterUserUsernameExists() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        String result = userService.registerUser(user);

        // Assert
        assertEquals("Username already exists.", result);
        verify(userRepository, never()).save(any(User.class));
        verify(bankAccountService, never()).createBankAccount(any(User.class));
        verify(notificationService, never()).createNotification(any(User.class), any(), anyString());
    }

    @Test
    public void testLoginUserSuccess() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("generatedToken");

        // Act
        String result = userService.loginUser(user);

        // Assert
        assertEquals("generatedToken", result);
        verify(notificationService, times(1)).createNotification(user, null, "testuser has logged in.");
    }

    @Test
    public void testLoginUserInvalidCredentials() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password", "password")).thenReturn(false);

        // Act
        String result = userService.loginUser(user);

        // Assert
        assertEquals("Invalid username or password.", result);
        verify(jwtUtil, never()).generateToken(anyString());
        verify(notificationService, never()).createNotification(any(User.class), any(), anyString());
    }
}