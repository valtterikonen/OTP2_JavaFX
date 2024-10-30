/*package com.example.easybankproject.controllertests;

import com.example.easybankproject.controllers.BankAccountController;
import com.example.easybankproject.db.BankAccountRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.User;
import com.example.easybankproject.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankAccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private BankAccountController bankAccountController;

    private User user;
    private BankAccount bankAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();

        user = new User();
        user.setUsername("testuser");

        bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    public void testGetBalanceSuccess() throws Exception {
        // Arrange
        String token = "Bearer validToken";
        when(jwtUtil.extractUsername("validToken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bankAccountRepository.findByUser(user)).thenReturn(Optional.of(bankAccount));

        // Act & Assert
        mockMvc.perform(get("/api/bankaccount/balance")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

}*/
package com.example.easybankproject.controllertests;

import com.example.easybankproject.controllers.BankAccountController;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.services.BankAccountService;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankAccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountController bankAccountController;

    @Mock
    private VaadinSession vaadinSession;

    private BankAccount bankAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();

        bankAccount = new BankAccount();
        bankAccount.setBalance(BigDecimal.valueOf(1000));

        // Mock VaadinSession
        VaadinSession.setCurrent(vaadinSession);
    }

    @Test
    public void testGetBalanceSuccess() throws Exception {
        String token = "Bearer validToken";
        when(bankAccountService.getBalance("validToken")).thenReturn(bankAccount);

        mockMvc.perform(get("/api/bankaccount/balance")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    public void testGetBalanceUnauthorized() throws Exception {
        String token = "Bearer invalidToken";
        when(bankAccountService.getBalance("invalidToken")).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/bankaccount/balance")
                        .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateBankAccountSuccess() throws Exception {
        when(vaadinSession.getAttribute("username")).thenReturn("testuser");
        when(bankAccountService.createBankAccount(any(BankAccount.class), eq("testuser"))).thenReturn("Bank account created");

        mockMvc.perform(post("/api/bankaccount/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Bank account created"));
    }

    @Test
    public void testCreateBankAccountUnauthorizedNoUser() throws Exception {
        when(vaadinSession.getAttribute("username")).thenReturn(null);

        mockMvc.perform(post("/api/bankaccount/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":1000}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Unauthorized: No user found in session."));
    }

    @Test
    public void testCreateBankAccountUnauthorizedException() throws Exception {
        when(vaadinSession.getAttribute("username")).thenReturn("testuser");
        when(bankAccountService.createBankAccount(any(BankAccount.class), eq("testuser"))).thenThrow(new RuntimeException("Error creating account"));

        mockMvc.perform(post("/api/bankaccount/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\":1000}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("Error creating account"));
    }
}