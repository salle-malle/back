package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.common.util.CoolSmsUtil;
import com.shinhan.pda_midterm_project.domain.auth.CertificationNumberPublisher;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.auth.repository.SmsCertificationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {

    private final CoolSmsUtil smsUtil;
    private final SmsCertificationDao smsCertificationDao;
    private final CertificationNumberPublisher certificationNumberPublisher;

    @Override
    public void issueCertificationSms(String phone) {
        String code = certificationNumberPublisher.publish().toString();
        smsCertificationDao.deleteByKey(phone);

        smsUtil.sendSms(phone, code);
        smsCertificationDao.save(phone, code);
    }


    @Override
    public void verifyNumber(String expectedCode, String phoneNumber) {

        String actualCode = smsCertificationDao.getByKey(phoneNumber);

        if (actualCode == null) {
            throw new AuthException(ResponseMessages.CERTIFICATION_BAD_REQUEST);
        }

        if (!actualCode.equals(expectedCode)) {
            throw new AuthException(ResponseMessages.INVALID_CERTIFICATION_NUMBER);
        }
    }
}
