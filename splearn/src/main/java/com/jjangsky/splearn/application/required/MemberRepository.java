package com.jjangsky.splearn.application.required;


import com.jjangsky.splearn.domain.Member;
import org.springframework.data.repository.Repository;

/**
 * Application 계층에서 외부 기술에 의존하는거 아님?
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save (Member member);
}
