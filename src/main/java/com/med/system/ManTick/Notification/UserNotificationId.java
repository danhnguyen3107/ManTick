package com.med.system.ManTick.Notification;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserNotificationId implements Serializable {
    private Long userId;
    private Long notificationId;
}