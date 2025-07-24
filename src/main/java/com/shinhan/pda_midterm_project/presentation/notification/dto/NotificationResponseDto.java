package com.shinhan.pda_midterm_project.presentation.notification.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponseDto(
        Long id,
        String title,
        String message,
        Boolean read,
        LocalDateTime time,
        String type
) {}