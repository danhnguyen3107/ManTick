

package com.med.system.ManTick.Notification.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.med.system.ManTick.Notification.Notification;
import com.med.system.ManTick.Notification.RequestResponse.SendNotificationRequest;
import com.med.system.ManTick.Notification.RequestResponse.SendNotificationRequestByrole;
import com.med.system.ManTick.Notification.Service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/sendNotification")
    public ResponseEntity<?> sendNotificationToUsers(@RequestBody SendNotificationRequest request) {

        Notification notification = notificationService.sendNotificationToUsers(request);

        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setMessage(notification.getMessage());

        // sendNotificationRequest.setUserIds(notification.getUserNotifications().stream()
        //                     .map(UserAndNotification::getUser)
        //                     .map(User::getId)
        //                     // .map(Long::valueOf)
        //                     .toList());

        return ResponseEntity.ok(sendNotificationRequest);
    }

    @PostMapping("/sendNotificationByRoles")
    public ResponseEntity<?> sendNotificationToUsers(@RequestBody SendNotificationRequestByrole request) {

        Notification notification = notificationService.sendNotificationToUsersByroles(request.getMessage(), request.getRoles());

        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setMessage(notification.getMessage());


        return ResponseEntity.ok(sendNotificationRequest);
    }
}
