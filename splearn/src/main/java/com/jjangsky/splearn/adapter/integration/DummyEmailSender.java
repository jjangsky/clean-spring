package com.jjangsky.splearn.adapter.integration;

import com.jjangsky.splearn.application.required.EmailSender;
import com.jjangsky.splearn.domain.Email;

public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("Dummy");

    }
}
