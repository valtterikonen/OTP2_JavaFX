
package com.example.easybankproject.services;

import com.example.easybankproject.db.NotificationRepository;
import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.Notification;
import com.example.easybankproject.models.Transaction;
import com.example.easybankproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Notification> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username);
        return notificationRepository.findByUser(user);
    }

    public int getNotificationsCount(String username) {
        User user = userRepository.findByUsername(username);
        List<Notification> notifications = notificationRepository.findByUser(user);
        return notifications.size();
    }

    public void createNotification(User user, Transaction transaction, String content) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTransaction(transaction);
        notification.setContent(content);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public boolean deleteNotification(Integer id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}