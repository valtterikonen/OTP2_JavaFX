package com.example.easybankproject.repositorytests;


import com.example.easybankproject.db.BankAccountRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BankaccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private BankAccount bankAccount;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("test1user");
        user.setPassword("password123");
        user.setEmail("test1user@example.com");
        user.setFirstname("Test");
        user.setLastname("User");
        user.setPhonenumber(1234567890);
        user.setAddress("123 Test St");
        user = userRepository.save(user);

        bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(BigDecimal.valueOf(1000));
        bankAccount = bankAccountRepository.save(bankAccount);
    }

    @Test
    public void testFindByUser() {
        Optional<BankAccount> foundAccount = bankAccountRepository.findByUser(user);
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getUser().getUsername()).isEqualTo("test1user");
    }

    @Test
    public void testFindByBankAccountId() {
        Optional<BankAccount> foundAccount = bankAccountRepository.findByBankAccountId(bankAccount.getBankAccountId());
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }
}