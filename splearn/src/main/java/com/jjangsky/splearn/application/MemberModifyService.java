package com.jjangsky.splearn.application;

import com.jjangsky.splearn.application.provided.MemberFinder;
import com.jjangsky.splearn.application.provided.MemberRegister;
import com.jjangsky.splearn.application.required.EmailSender;
import com.jjangsky.splearn.application.required.MemberRepository;
import com.jjangsky.splearn.domain.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 회원 관련 작업이 진행할 수록 provided interface가 점점 증가한다.
 * 해당 인터페이스가 만들어졌다는 것은, 컨트롤러 레이어에서 사용될 객체(Bean)로 활용될 가능성이 있으며,
 * 용도에 따라 다양한 구현체로 분리되어 관리될 수 있다는 의미
 *
 * 이렇게 하나의 클래스가 무거워져버리면 인터페이스를 분리해야 하는 과정이 필요함
 */

@Service
@Transactional // AOP 기술을 활용하여 메소드가 시작 전 트랜잭션 시작하고, 종료 후 마무리함
@RequiredArgsConstructor
@Validated // -> 트랜잭션과 비슷하여 시작 전 파라미터 정보 확인 처리
public class MemberModifyService implements MemberRegister {

    /**
     * 필드를 사용할 때 변경할 일이 없으므로 final을 생성한다.
     * 굳이 강제는 아니지만 장점이 존재한다.
     * final 때문에 멤머 서비스에 오브젝트가 만들어지는 시점에 반드시 초기화가 필요 -> 컴파일 에러 발생
     * (해당 필드를 주입하지 않았을 때 미리 코드 레벨 단계에서 알 수 있음)
     */
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    /**
     * 서비스 로직 처리 과정
     * check - 현재 시스템의 상태, 파리미터에서 넘어오는 정보
     * domain Model - 주요 로직 처리
     * repository - 저장 및 외부 작업 처리
     * post process - 후 처리 작업 진행 (기록 처리)
     */

    @Override
    public Member register(@Valid MemberRegisterRequest registerRequest) {
        // check - 현재 시스템의 상태, 파리미터에서 넘어오는 정보


        /**
         * 검증에 대한 고찰
         *
         * 하나의 API 요청을 수행 하면서 여러 계층을 거치고 그 계층을 건널 때 마다
         * 사용자로 부터 받아온 파라미터에 대한 검증을 처리할 수 있는 부분들이 존재한다.
         * 이건 주로 어디서 해야 할까? -> 서비스 로직에서 하는게 좋다.
         * 보통 서비스 넘어오기 직전에 `@Valid` 를 사용해서 처리함
         *
         * 도메인 내부에서도 검증을 할 수 있지만 최소한의 검증을 하는 것이 좋음
         */


        // 코드 읽는 것에 대해 불편함이 존재하여 메소드 내부 추상화 레벨을 맞춤
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        // 어떤 제목과 어떤 내용을 보낸다는 것을 디테일한 내용이라 추상화 레벨을 맞춤
        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);
        /**
         * 자신의 포트에서 memberFinder 사용중이니 가져와서 구현 처리
         * Member member = memberRepository.findById(memberId)
         *                 .orElseThrow(() -> new IllegalArgumentException("Invalid member id: " + memberId));
         */
        member.activate();

        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일 입니다.");
        }
    }
}
