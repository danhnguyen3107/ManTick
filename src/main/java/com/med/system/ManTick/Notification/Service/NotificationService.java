package com.med.system.ManTick.Notification.Service;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import com.med.system.ManTick.Notification.Notification;
import com.med.system.ManTick.Notification.UserAndNotification;
import com.med.system.ManTick.Notification.UserNotificationId;
import com.med.system.ManTick.Notification.Repository.NotificationRepository;
import com.med.system.ManTick.Notification.Repository.UserAndNotificationRepository;
import com.med.system.ManTick.Notification.RequestResponse.SendNotificationRequest;
import com.med.system.ManTick.Users.Role;
import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService  {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserAndNotificationRepository userNotificationRepository;

    @Transactional
    public Notification sendNotificationToUsers(SendNotificationRequest request) {
        // Create and save the notification entity first.
        Notification notification = Notification.builder()
                .message(request.getMessage())
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
        notification = notificationRepository.save(notification);

        // For each user ID, create a join entity.
        for (Long userId : request.getUserIds()) {
            User user = userRepository.findById(userId.intValue())
                    .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));

            UserNotificationId userNotificationId = new UserNotificationId(user.getId().longValue(), notification.getId());
            UserAndNotification userNotification = UserAndNotification.builder()
                    .id(userNotificationId)
                    .user(user)
                    .notification(notification)
                    .isRead(false)
                    .build();

            userNotificationRepository.save(userNotification);
        }

        return notification;
    }

    @Transactional
    public Notification sendNotificationToUsersByroles(String message, Set<Role> roles) {
        // Create and save the notification entity first.
        Notification notification = Notification.builder()
                .message(message)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();
        notification = notificationRepository.save(notification);

        // Fetch users by roles
        Set<User> users = new HashSet<>();
        if (roles != null && ! roles.isEmpty()) {
            for (Role role : roles) {
                users.addAll(userRepository.findByRole(role));
            }
        }


        // For each user, create a join entity.
        for (User user : users) {
            UserNotificationId userNotificationId = new UserNotificationId(user.getId().longValue(), notification.getId());
            UserAndNotification userNotification = UserAndNotification.builder()
                    .id(userNotificationId)
                    .user(user)
                    .notification(notification)
                    .isRead(false)
                    .build();

            userNotificationRepository.save(userNotification);
        }

        return notification;
    }
    
}
