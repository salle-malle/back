package com.shinhan.pda_midterm_project.common.util;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CoolSmsUtil {

    private static final String URI = "https://api.coolsms.co.kr";
    private static final String MESSAGE_FORMAT = "[BOLLE MALLE] 본인 확인 인증번호는 [%s]입니다.";

    @Value("${coolSMS.apiKey}")
    private String apiKey;

    @Value("${coolSMS.secretKey}")
    private String secretKey;

    @Value("${coolSMS.from}")
    private String from;

    DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, URI);
    }

    public void sendSms(String to, String code) {
        String content = String.format(MESSAGE_FORMAT, code);
        Message message = new Message();

        message.setFrom(from);
        message.setTo(to);
        message.setText(content);

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        messageService.sendOne(request);
    }
}
