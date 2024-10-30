package com.example.easybankproject.repositorytests;


import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

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
    }

    @Test
    public void testFindByUsername() {
        User foundUser = userRepository.findByUsername("test1user");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("test1user");
    }
}
