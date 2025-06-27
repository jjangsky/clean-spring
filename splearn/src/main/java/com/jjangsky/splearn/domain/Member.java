package com.jjangsky.splearn.domain;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.util.Objects;

import static org.springframework.util.Assert.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 보호 레벨을 최소 protected로 설저
@NaturalIdCache // 영속성 컨텍스트에서 캐싱하는건 Id 기준이지만 해당 어노테이션 사용하면 NaturalId로도 영속성 컨텍스트에 캐싱이 가능하다.
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // email 필드에 대해서 VO 로 변환
    @Embedded
    @NaturalId   // 비즈니스 적으로 의미가 있는 필드에 적용
    private Email email;
    private String nickname;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;


    // 생성자 자체는 접근을 차단하고 정적 팩토리 메소드를 통해서만 접근할 수 있도록
    @Builder
    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(Objects.requireNonNull(createRequest.email())); // 값이 null 이 들어오면 npe 발생
        member.nickname = Objects.requireNonNull(createRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;

        return member;
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

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }

    public boolean isActive() {
        return  this.status == MemberStatus.ACTIVE;
    }
}
