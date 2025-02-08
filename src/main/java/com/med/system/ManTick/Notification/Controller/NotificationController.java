

package com.med.system.ManTick.Notification.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.med.system.ManTick.Notification.Notification;
import com.med.system.ManTick.Notification.RequestResponse.SendNotificationRequest;
import com.med.system.ManTick.Notification.Service.NotificationService;
import com.med.system.ManTick.Users.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @PostMapping("/sendNotification")
    public ResponseEntity<Notification> sendNotificationToUsers(SendNotificationRequest request) {
        Notification notification = notificationService.sendNotificationToUsers(request);
        return ResponseEntity.ok(notification);
    }
}
