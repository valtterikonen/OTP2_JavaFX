
package com.example.easybankproject.repositorytests;

import com.example.easybankproject.db.NotificationRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.BankAccount;
import com.example.easybankproject.models.Notification;
import com.example.easybankproject.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Notification notification;


    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("test1user");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setFirstname("Test");
        user.setLastname("User");
        user.setPhonenumber(1234567890);
        user.setAddress("123 Test St");
        user = userRepository.save(user);

        notification = new Notification();
        notification.setUser(user);
        notification.setContent("Test notification");
        notification.setTimestamp(LocalDateTime.now());
        notification = notificationRepository.save(notification);
    }

    @Test
    public void testFindByUser() {
        List<Notification> notifications = notificationRepository.findByUser(user);
        assertThat(notifications).isNotEmpty();
        assertThat(notifications.get(0).getContent()).isEqualTo("Test notification");
    }
}