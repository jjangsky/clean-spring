package com.jjangsky.splearn.domain;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

import static org.springframework.util.Assert.*;

@Getter
@ToString
public class Member {
    private String email;
    private String nickname;
    private String passwordHash;
    private MemberStatus status;

    public Member(String email, String nickname, String passwordHash) {
        this.email = Objects.requireNonNull(email); // 값이 null 이 들어오면 npe 발생
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    public void activate() {
        /**
         * 하단의 방식 처럼 IF 를 사용하여 로직을 처리하는 것도 좋지만
         * 이러한 자잘한 분기 처리가 계속 쌓이면 결국에는 핵심 비즈니스 로직을 이해하는데 방해가 된다.
         *
         * 따라서 가독성이 있게 Assert를 사용하여 상태를 정의 내리는 것도 좋은 방법이다.
         */
//        if(status == MemberStatus.PENDING) throw new IllegalArgumentException("PENDING 상태가 아닙니다.");
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
    }
}
