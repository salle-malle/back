package com.shinhan.pda_midterm_project.domain.notification.model;

import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT", length = 50)
    private String notificationTitle;

    @Column(columnDefinition = "TEXT", length = 255)
    private String notificationContent;

    @Column
    private Boolean notificationIsRead;

    @Column(columnDefinition = "TEXT")
    private String notificationUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType notificationType;

    public void markAsRead() {
        this.notificationIsRead = true;
    }
}