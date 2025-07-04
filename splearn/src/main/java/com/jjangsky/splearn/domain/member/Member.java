package com.jjangsky.splearn.domain.member;
import com.jjangsky.splearn.domain.AbstractEntity;
import com.jjangsky.splearn.domain.shared.Email;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.util.Objects;

import static org.springframework.util.Assert.*;

@Entity
//@Table(name = "members", uniqueConstraints = {
//        @UniqueConstraint(name = "UK_MEMBER_EMAIL_ADDRESS", columnNames = "email_address")
//})
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 보호 레벨을 최소 protected로 설저
@NaturalIdCache // 영속성 컨텍스트에서 캐싱하는건 Id 기준이지만 해당 어노테이션 사용하면 NaturalId로도 영속성 컨텍스트에 캐싱이 가능하다.
public class Member extends AbstractEntity {

    /**
     * 도메인 객체와 엔티티 객체를 하나로 사용하는 Tip
     *
     * 엔티티 객체로 사용하게 되면 관련 어노테이션들이 너무 많아서 코드를 읽는데 방해가 됨.
     * 설정에 관한 어노테이션(`@Table`, `@Column`, `@Enumerated` 등)들은 별도의 XML로 분리하여 적용할 수 있음
     * (XML 설정을 사용하면 도메인 객체와 엔티티 객체를 분리할 수 있으며, XML 파일이 어노테이션 설정을 오버라이드 한다.)
     * 단, 도메인 모델 관점에서 의미 있는 설정(ManyToOne 과 같은 관계, natural id 등)은 남겨 놓아야 한다.
     */

    // email 필드에 대해서 VO 로 변환
//    @Embedded
    @NaturalId   // 비즈니스 적으로 의미가 있는 필드에 적용
    private Email email;

//    @Column(length = 100, nullable = false)
    private String nickname;

//    @Column(length = 200, nullable = false)
    private String passwordHash;

//    @Enumerated(EnumType.STRING)
//    @Column(length = 20, nullable = false)
    private MemberStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberDetail detail;


    // 생성자 자체는 접근을 차단하고 정적 팩토리 메소드를 통해서만 접근할 수 있도록
    @Builder
    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(Objects.requireNonNull(createRequest.email())); // 값이 null 이 들어오면 npe 발생
        member.nickname = Objects.requireNonNull(createRequest.nickname());
        member.passwordHash = Objects.requireNonNull(passwordEncoder.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

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
        this.detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.nickname = Objects.requireNonNull(updateRequest.nickname());
        this.detail.updateInfo(updateRequest);
    }


    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(Objects.requireNonNull(password));
    }

    public boolean isActive() {
        return  this.status == MemberStatus.ACTIVE;
    }
}
