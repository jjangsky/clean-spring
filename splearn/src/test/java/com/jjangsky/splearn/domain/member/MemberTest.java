package com.jjangsky.splearn.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.jjangsky.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static com.jjangsky.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterRequest(), passwordEncoder);
    }

    @Test
    void registerMember() {

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();

    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail() {
        /**
         * 이미 활성화 된 회원에 대해서 다시 활성화를 시키는 경우에 대해서 대처하는 테스트 코드
         * 무의미하지 않겠냐? -> 추후 버그 가능성 농후
         */
        member.activate();

        assertThatThrownBy(() -> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }

    /**
     * 테스트 코드 작성할 때 생각할 것.
     * 1. Given - when - then을 주석으로 정의하며 규칙을 지켜 개발하는 것도 좋지만
     *    너무 규칙을 맞출 필요는 없다. 대충 봐도 알만하게 작성해야 함 (초보들이나 bdd 씀)
     * 2. 간단한 테스트 라면 하나의 테스트 코드에 여러 가지 case를 넣어도 댐
     * 3. `@Display`로 테스트 케이스에 대한 설명을 적는데 상황을 잘 구분 해야함
     *     보통 테스트 코드는 한번 작성하고 다시 안보는데 굳이 작성해야하나?
     *     -> 실패한 Case에 대해서만 재확인 해도 되며 복합적으로 복잡한 Case 에만 추가해도 됨
     */

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secret1234", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(member.getNickname()).isEqualTo("jjangsky");

        member.changeNickname("horokskyy");

        assertThat(member.getNickname()).isEqualTo("horokskyy");
    }

    @Test
    void changePassword() {
        member.changePassword("verysecret2", passwordEncoder);
        assertThat(member.verifyPassword("verysecret2", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        // 회원 내부 상태의 변화를 외부의 코드로 체크하지 말자 -> 중복 가능성이 높고 결국에는 응집도가 높아짐
        // -> getter로 꺼내서 외부에서 비교 하는 것이 아닌 객체 내부에서 비교
        assertThat(member.isActive()).isFalse();

        member.activate();
        assertThat(member.isActive()).isTrue();

        member.deactivate();
        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(() -> {
            Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);

        Member.register(createMemberRegisterRequest(), passwordEncoder);
    }

    @Test
    void updateInfo() {
        member.activate();

        var request = new MemberInfoUpdateRequest("newNickname", "newProfile", "newBio");
        member.updateInfo(request);

        assertThat(member.getNickname()).isEqualTo("newNickname");
        assertThat(member.getDetail().getProfile().address()).isEqualTo("newProfile");
        assertThat(member.getDetail().getIntroduction()).isEqualTo("newBio");
    }

}