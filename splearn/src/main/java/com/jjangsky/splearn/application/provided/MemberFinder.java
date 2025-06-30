package com.jjangsky.splearn.application.provided;

import com.jjangsky.splearn.domain.Member;

/**
 * 회원을 조회
 */
public interface MemberFinder {
    Member find(Long memberId);
}
