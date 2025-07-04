package com.jjangsky.splearn.adapter.integration;

import com.jjangsky.splearn.domain.shared.Email;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DummyEmailSenderTest {
    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("jjangsky@github.io"), "subject", "body");

        assertThat(out.capturedLines()[0]).isEqualTo("Dummy");
    }
}