package com.jjangsky.splearn.application;

import com.jjangsky.splearn.application.provided.MemberFinder;
import com.jjangsky.splearn.application.required.MemberRepository;
import com.jjangsky.splearn.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member id: " + memberId));
    }
}
