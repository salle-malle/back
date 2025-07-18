package com.shinhan.pda_midterm_project.domain.auth.repository;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SmsCertificationDao {

    private static final String PREFIX = "phone:";
    private final Cache<String, String> smsCertificationCache;

    public void save(String phone, String code) {
        smsCertificationCache.put(PREFIX + phone, code);
    }

    public String getByKey(String phone) {
        return smsCertificationCache.getIfPresent(PREFIX + phone);
    }

    public void deleteByKey(String phone) {
        smsCertificationCache.invalidate(PREFIX + phone);
    }
}
