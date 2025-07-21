package com.jjangsky.splearn.application.member.provided;

import com.jjangsky.splearn.SplearnTestConfiguration;
import com.jjangsky.splearn.domain.member.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
record MemberRegisterTests(MemberRegister memberRegister, EntityManager entityManager) {

    /**
     * 왜 레코드 클래스에 오류가 생기는가? -> `@Transactional` 어노테이션이 레코드 클래스에 적용되지 않기 때문
     * 트랜잭셔널 어노테이션 자체가 서브 클래스를 만들어서 상속 기능을 사용하는 방식인데 record 같은 경우 불변객체라서
     * 상속할 수 없음 -> 상속 할 수 없는 클래스에 트랜잭셔널을 붙여 놨네?
     *
     * 하지만, 여기서 사용되는 트랜잭셔널은 AOP로 트랜잭션을 코드에 적용해주는 것이 아닌 테스팅 프로엠워크와 결합해서
     * 테스트 메소드가 실행 되기 전에 이벤트 메소드를 통해 초기화 해주는 역할이기 때문에 문제가 없음
     */

    @Test
    void register() {
        // SplearnTestConfiguration 테스트 의존성 주입 과정이 필요함
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        // TestConfiguration이 필요함 -> bean 등록 과정 필요
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    }

    @Test
    void duplicateEmailFail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void activate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member.updateInfo(new MemberInfoUpdateRequest("newNickname", "newprofile", "newBio"));
        assertThat(member.getDetail().getProfile().address()).isEqualTo("newprofile");
    }



    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }


    @Test
    void setMemberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("test@splearn.app", "Toby", "secret"));
        checkValidation(new MemberRegisterRequest("test@splearn.app", "jjangsky_test_12341234123", "secret"));
        checkValidation(new MemberRegisterRequest("test.app", "jjangsky", "secret"));
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void updateInfoFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        member = memberRegister.updateInfo(member.getId(),
                new MemberInfoUpdateRequest("Peter", "toby100", "자기소개입니다."));

        Member member2 = registerMember("toby2@splearn.app");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(),
                new MemberInfoUpdateRequest("James", "toby100", "자기소개입니다.")))
                .isInstanceOf(DuplicateProfileException.class)
                .hasMessageContaining("프로필 주소는 이미 사용중입니다.");
    }
}
