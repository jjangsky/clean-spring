package com.jjangsky.splearn.application;

import com.jjangsky.splearn.application.provided.MemberRegister;
import com.jjangsky.splearn.application.required.EmailSender;
import com.jjangsky.splearn.application.required.MemberRepository;
import com.jjangsky.splearn.domain.Member;
import com.jjangsky.splearn.domain.MemberRegisterRequest;
import com.jjangsky.splearn.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 관련 작업이 진행할 수록 provided interface가 점점 증가한다.
 * 해당 인터페이스가 만들어졌다는 것은, 컨트롤러 레이어에서 사용될 객체(Bean)로 활용될 가능성이 있으며,
 * 용도에 따라 다양한 구현체로 분리되어 관리될 수 있다는 의미
 *
 * 이렇게 하나의 클래스가 무거워져버리면 인터페이스를 분리해야 하는 과정이 필요함
 */

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {

    /**
     * 필드를 사용할 때 변경할 일이 없으므로 final을 생성한다.
     * 굳이 강제는 아니지만 장점이 존재한다.
     * final 때문에 멤머 서비스에 오브젝트가 만들어지는 시점에 반드시 초기화가 필요 -> 컴파일 에러 발생
     * (해당 필드를 주입하지 않았을 때 미리 코드 레벨 단계에서 알 수 있음)
     */
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
    public Member register(MemberRegisterRequest registerRequest) {
        // check - 현재 시스템의 상태, 파리미터에서 넘어오는 정보
        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

        return member;
    }
}
