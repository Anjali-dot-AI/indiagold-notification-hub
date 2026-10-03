package com.indiagold.notificationhub.repository;

import com.indiagold.notificationhub.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByRecipientUserIdOrderByTimestampDesc(Long recipientUserId);
}
