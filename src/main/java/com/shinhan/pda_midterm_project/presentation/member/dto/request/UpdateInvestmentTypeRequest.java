package com.shinhan.pda_midterm_project.presentation.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateInvestmentTypeRequest(
        @NotNull Long investmentTypeId
) {}