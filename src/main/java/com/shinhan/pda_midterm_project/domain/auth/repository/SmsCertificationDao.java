package com.shinhan.pda_midterm_project.domain.auth.repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SmsCertificationDao {

    private static final String PREFIX = "phone:";
    private static final long TTL_SECONDS = 5 * 60;

    private final Map<String, CacheValue> cache = new ConcurrentHashMap<>();

    public void save(String phone, String code) {
        long expireAt = Instant.now().getEpochSecond() + TTL_SECONDS;
        cache.put(PREFIX + phone, new CacheValue(code, expireAt));
    }

    public String getByKey(String phone) {
        String key = PREFIX + phone;
        CacheValue value = cache.get(key);
        if (value == null || value.isExpired()) {
            cache.remove(key); // 만료됐으면 삭제
            return null;
        }
        return value.code();
    }

    public void deleteByKey(String phone) {
        cache.remove(PREFIX + phone);
    }

    private record CacheValue(String code, long expireAtEpochSec) {
        boolean isExpired() {
            return Instant.now().getEpochSecond() > expireAtEpochSec;
        }
    }
}