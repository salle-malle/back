package com.shinhan.pda_midterm_project.domain.notification.repository;

import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
