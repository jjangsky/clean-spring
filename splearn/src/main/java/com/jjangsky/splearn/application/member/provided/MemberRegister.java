package com.jjangsky.splearn.application.member.provided;

import com.jjangsky.splearn.domain.member.Member;
import com.jjangsky.splearn.domain.member.MemberRegisterRequest;
import jakarta.validation.Valid;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {
    /**
     *  Q.Entity 객체를 리턴해도 되나? DTO로 변경해서 반환하는게 아니라?
     *
     *  의존관계 면에서 봤을 때 Entity를 반환시켜도 전혀 문제가 없다.
     *  -> 도메인 레이어에서 만들어지는 엔티티는 괜찮음
     */
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);


}
