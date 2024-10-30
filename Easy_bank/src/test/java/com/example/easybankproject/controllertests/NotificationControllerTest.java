package com.example.easybankproject.controllertests;

import com.example.easybankproject.controllers.NotificationController;
import com.example.easybankproject.models.Notification;
import com.example.easybankproject.services.NotificationService;
import com.example.easybankproject.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private NotificationController notificationController;

    private List<Notification> notifications;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();

        Notification notification1 = new Notification();
        notification1.setNotificationId(1);
        notification1.setContent("Notification 1");

        Notification notification2 = new Notification();
        notification2.setNotificationId(2);
        notification2.setContent("Notification 2");

        notifications = new ArrayList<>();
        notifications.add(notification1);
        notifications.add(notification2);
    }

    @Test
    public void testGetUserNotifications() throws Exception {
        String token = "Bearer validToken";
        when(jwtUtil.extractUsername("validToken")).thenReturn("testuser");
        when(notificationService.getUserNotifications("testuser")).thenReturn(notifications);

        mockMvc.perform(get("/api/notifications")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Notification 1"))
                .andExpect(jsonPath("$[1].content").value("Notification 2"));
    }

    @Test
    public void testGetNotificationsCount() throws Exception {
        String token = "Bearer validToken";
        when(jwtUtil.extractUsername("validToken")).thenReturn("testuser");
        when(notificationService.getNotificationsCount("testuser")).thenReturn(notifications.size());

        mockMvc.perform(get("/api/notifications/count")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void testDeleteNotificationSuccess() throws Exception {
        int notificationId = 1;
        when(notificationService.deleteNotification(notificationId)).thenReturn(true);

        mockMvc.perform(delete("/api/notifications/{id}", notificationId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotificationNotFound() throws Exception {
        int notificationId = 1;
        when(notificationService.deleteNotification(notificationId)).thenReturn(false);

        mockMvc.perform(delete("/api/notifications/{id}", notificationId))
                .andExpect(status().isNotFound());
    }
}

