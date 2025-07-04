package com.jjangsky.splearn.application.member.provided;

import com.jjangsky.splearn.domain.member.Member;

/**
 * 회원을 조회
 */
public interface MemberFinder {
    Member find(Long memberId);
}
