package com.shinhan.pda_midterm_project.domain.disclosure.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventType {
    ITEM_5_02("임원 변경", "Executive Change"),
    ITEM_2_02("실적 발표", "Earnings Report"),
    ITEM_2_01("자산 인수/매각", "Asset Acquisition/Disposition"),
    ITEM_1_01("중요 계약 체결", "Material Contract"),
    ITEM_1_02("계약 종료", "Contract Termination"),
    ITEM_4_01("감사인 변경", "Auditor Change"),
    ITEM_8_01("기타 중요 사건", "Other Important Event");

    private final String korDescription;
    private final String engDescription;
}
