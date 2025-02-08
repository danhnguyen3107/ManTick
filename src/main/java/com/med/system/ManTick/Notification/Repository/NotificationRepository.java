package com.med.system.ManTick.Notification.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import com.med.system.ManTick.Notification.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
