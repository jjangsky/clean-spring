package com.jjangsky.splearn.domain.member;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Profile(String address) {
    private static final Pattern PROFILE_ADDRESS_PATTERN =
            Pattern.compile("[a-z0-9]+");

    public Profile {
        // record class를 사용하는 경우 생성자를 생성할때 따로 인자를 명시하지 않아도 됨
        if (address == null || (!address.isEmpty() && !PROFILE_ADDRESS_PATTERN.matcher(address).matches())) {
            throw new IllegalArgumentException("프로필 주소 형식이 바르지 않습니다. " + address);
        }

        if (address.length() > 15) {
            throw new IllegalArgumentException("프로필 주소는 155자 이하로 입력해주세요. " + address);
        }
    }

    public String url() {
        return "@" + address;
    }
}
