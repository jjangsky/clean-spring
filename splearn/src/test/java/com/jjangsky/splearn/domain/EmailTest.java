package com.jjangsky.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    @Test
    void equality() {
        // 객체에 대한 동등성 비교 테스트
        // VO 객체로 만들어 있어서 동일성 까지 비교할 필요는 없음
        var email1 = new Email("test@splearn.app");
        var email2 = new Email("test@splearn.app");

        assertEquals(email1, email2);
    }

}