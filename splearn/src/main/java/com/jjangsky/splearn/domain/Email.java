package com.jjangsky.splearn.domain;

import java.util.regex.Pattern;

public record Email(String address) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        // record class를 사용하는 경우 생성자를 생성할때 따로 인자를 명시하지 않아도 됨
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("이메일 형식이 바르지 않습니다. " + address);
        }
    }
}
