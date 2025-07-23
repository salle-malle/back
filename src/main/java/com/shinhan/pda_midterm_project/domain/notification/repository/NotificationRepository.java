package com.shinhan.pda_midterm_project.domain.notification.repository;

import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);

    List<Notification> findByMemberId(Long memberId);

    boolean existsByMemberIdAndNotificationIsReadFalse(Long memberId);
}
