package com.shinhan.pda_midterm_project.domain.auth.model;

public record Accessor(AccessorRole accessorRole, Long memberId) {
    private static final Long GUEST_ID = 0L;

    public static Accessor member(Long memberId) {
        return Accessor.of(AccessorRole.MEMBER, memberId);
    }

    public static Accessor guest() {
        return Accessor.of(AccessorRole.GUEST, GUEST_ID);
    }

    public static Accessor admin(Long memberId) {
        return Accessor.of(AccessorRole.ADMIN, memberId);
    }

    public static Accessor of(AccessorRole accessRole, Long memberId) {
        return new Accessor(accessRole, memberId);
    }

    public Boolean isMember() {
        return accessorRole == AccessorRole.MEMBER;
    }
}
