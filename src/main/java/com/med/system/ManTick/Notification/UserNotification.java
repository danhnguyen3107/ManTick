package com.med.system.ManTick.Notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import com.med.system.ManTick.Users.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_notification")
public class UserNotification {

    // @EmbeddedId
    // private UserNotificationId id;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    // @ManyToOne(fetch = FetchType.LAZY)
    // @MapsId("userId")
    // @JoinColumn(name = "user_id")
    // private User user;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @MapsId("notificationId")
    // @JoinColumn(name = "notification_id")
    // private Notification notification;

    // Additional field for tracking whether the user has read the notification
    @Column(nullable = false)
    private boolean read;

    // Optionally, add a field to track when the notification was read
    private Date readAt;
}