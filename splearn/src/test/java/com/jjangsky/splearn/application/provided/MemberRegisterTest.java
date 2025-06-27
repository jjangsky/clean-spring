package com.jjangsky.splearn.application.provided;

import com.jjangsky.splearn.application.MemberService;
import com.jjangsky.splearn.application.required.EmailSender;
import com.jjangsky.splearn.application.required.MemberRepository;
import com.jjangsky.splearn.domain.Email;
import com.jjangsky.splearn.domain.Member;
import com.jjangsky.splearn.domain.MemberFixture;
import com.jjangsky.splearn.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterTest {
    /**
     * 테스트를 작성할때는 인터페이스를 기준으로 테스트를 진행해야 된다.
     *
     * 구현체에 대해서 테스트 class 생성이 아닌 인터페이스 기준으로 테스트 class를 만들어서
     * 테스트 동작 시, 구현체를 만들어 구현체의 내부 로직을 검증해야 한다.
     */

    @Test
    void register() {
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
        );
        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMock() {
        EmailSenderMock emailSenderMock = new EmailSenderMock();
        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );
        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);

        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );
        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
    }

    static class  MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            /**
             * member 객체에 id값을 설정해야 하는데 private로 되어 있는 경우
             * ReflectionTestUtils로 필드의 값을 설정할 수 있음 - 테스트 전용 용도
             */
            ReflectionTestUtils.setField(member, "id", 1);
            return member;
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {
        }
    }

    static class EmailSenderMock implements EmailSender {
        List<Email> tos = new ArrayList<>();
        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }

        public List<Email> getTos() {
            return tos;
        }
    }

}