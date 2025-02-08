

package com.med.system.ManTick.Notification.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.med.system.ManTick.Notification.UserAndNotification;
import com.med.system.ManTick.Notification.UserNotificationId;

public interface UserAndNotificationRepository extends JpaRepository<UserAndNotification, UserNotificationId> {

    
} 