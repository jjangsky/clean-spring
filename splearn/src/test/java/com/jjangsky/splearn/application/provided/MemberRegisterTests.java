package com.jjangsky.splearn.application.provided;

import com.jjangsky.splearn.SplearnTestConfiguration;
import com.jjangsky.splearn.domain.*;
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
record MemberRegisterTests(MemberRegister memberRegister) {

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
    void setMemberRegisterRequestFail() {
        extracted(new MemberRegisterRequest("test@splearn.app", "Toby", "secret"));
        extracted(new MemberRegisterRequest("test@splearn.app", "jjangsky_test_12341234123", "secret"));
        extracted(new MemberRegisterRequest("test.app", "jjangsky", "secret"));
    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
