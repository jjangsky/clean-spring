package com.jjangsky.splearn.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MemberRegisterRequest(
        @Email
        String email,

        @Size(min = 5, max = 20)
        String nickname,

        @Size(min = 8, max = 100)
        String password
) {
    /**
     * JSR-303 Bean validation
     * 자바 빈 형태로 프로퍼티를 만들어서 데이터를 전달할 때 이걸 검증하도록 만들이진 표준
     */
}
