package com.jjangsky.splearn.application.required;

import com.jjangsky.splearn.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.jjangsky.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static com.jjangsky.splearn.domain.MemberFixture.createPasswordEncoder;


@DataJpaTest
class MemberRepositoryTest{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void createMember() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        memberRepository.save(member);

        entityManager.flush();
    }



}