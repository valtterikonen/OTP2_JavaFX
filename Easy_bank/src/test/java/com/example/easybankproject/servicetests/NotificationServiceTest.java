package com.example.easybankproject.servicetests;

import com.example.easybankproject.db.NotificationRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.Notification;
import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.models.User;
import com.example.easybankproject.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
    private Notification notification;
    private Transaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testuser");

        transaction = new Transaction();
        transaction.setTransactionId(1);

        notification = new Notification();
        notification.setUser(user);
        notification.setTransaction(transaction);
        notification.setContent("Test notification");
        notification.setTimestamp(LocalDateTime.now());
    }

    @Test
    public void testGetUserNotifications() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        when(notificationRepository.findByUser(user)).thenReturn(notifications);

        // Act
        List<Notification> result = notificationService.getUserNotifications("testuser");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test notification", result.get(0).getContent());
    }

    @Test
    public void testGetNotificationsCount() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        when(notificationRepository.findByUser(user)).thenReturn(notifications);

        // Act
        int count = notificationService.getNotificationsCount("testuser");

        // Assert
        assertEquals(1, count);
    }

    @Test
    public void testCreateNotification() {
        // Act
        notificationService.createNotification(user, transaction, "Test notification");

        // Assert
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void testDeleteNotificationSuccess() {
        // Arrange
        when(notificationRepository.existsById(1)).thenReturn(true);

        // Act
        boolean result = notificationService.deleteNotification(1);

        // Assert
        assertTrue(result);
        verify(notificationRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteNotificationFailure() {
        // Arrange
        when(notificationRepository.existsById(1)).thenReturn(false);

        // Act
        boolean result = notificationService.deleteNotification(1);

        // Assert
        assertFalse(result);
        verify(notificationRepository, never()).deleteById(1);
    }
}