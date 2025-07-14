package com.shinhan.pda_midterm_project.domain.auth;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class SecretRandomPublisher implements CertificationNumberPublisher {

    private static final int RANGE_START = 100000;
    private static final int RANGE_END = 999999;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Integer publish() {
        return RANGE_START + secureRandom.nextInt(RANGE_END - RANGE_START + 1);
    }
}
