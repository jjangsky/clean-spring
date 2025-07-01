package com.jjangsky.splearn.adapter.integration;

import com.jjangsky.splearn.application.required.EmailSender;
import com.jjangsky.splearn.domain.Email;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;

@Component
@Fallback
/**
 * @Fallback
 * 다른 빈을 다 찾다가 이 타입의 빈이 없을 경우, 대체로 해당 빈을 사용
 * 빈에 대해서 우선 순위를 지정할 때 Primary 등 지정을 하는데
 * 없을 경우 해당 빈을 사용
 */
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("Dummy");
    }
}
