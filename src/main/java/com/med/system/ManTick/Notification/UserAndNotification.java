
package com.med.system.ManTick.Notification;

import java.sql.Date;
import com.med.system.ManTick.Users.User;
import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAndNotification {


    @EmbeddedId
    private UserNotificationId id;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("notificationId")
    @JoinColumn(name = "notification_id")
    private Notification notification;

    
    @Column(name = "is_read", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isRead;

    // Optionally, add a field to track when the notification was read
    private Date readAt;
}
