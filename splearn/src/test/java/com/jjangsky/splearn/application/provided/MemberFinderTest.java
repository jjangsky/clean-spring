package com.jjangsky.splearn.application.provided;

import com.jjangsky.splearn.SplearnTestConfiguration;
import com.jjangsky.splearn.domain.Member;
import com.jjangsky.splearn.domain.MemberFixture;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager entityManager) {
    /**
     * 테스트의 기준은 인터페이스로 작성한다. -> 구현 클래스에 대해서는 관심이 없음
     *
     * 조회 로직과 쓰기 로직을 분리하는 이유?
     * 시간이 지나면서 의존하는 오브젝트 들의 종류가 달라져서 혼란스러움 (CQS 패턴 적용)
     */

    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(member.getId()).isEqualTo(found.getId());
    }

    @Test
    void findFail() {
        assertThatThrownBy(() -> memberFinder.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }


}