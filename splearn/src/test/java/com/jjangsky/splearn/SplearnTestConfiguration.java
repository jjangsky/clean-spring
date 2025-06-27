package com.jjangsky.splearn;

import com.jjangsky.splearn.application.required.EmailSender;
import com.jjangsky.splearn.domain.Email;
import com.jjangsky.splearn.domain.MemberFixture;
import com.jjangsky.splearn.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
// 해당 어노테이션을 붙이면 내부에 빈이 붙은 팩토리 메소드 들을 만들어줌
// 메소드들을 실행한 결과를 스프링 빈으로 등록하여 사용함
public class SplearnTestConfiguration {
        @Bean
        public EmailSender emailSender() {
            return new EmailSender() {
                @Override
                public void send(Email email, String subject, String body) {
                    System.out.println("Sending Email: " + email);
                }
            };
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return MemberFixture.createPasswordEncoder();
        }
}
