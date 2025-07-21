package com.jjangsky.splearn.application.member.required;


import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import com.jjangsky.splearn.domain.member.Profile;
import com.jjangsky.splearn.domain.shared.Email;
import com.jjangsky.splearn.domain.member.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Application 계층에서 외부 기술에 의존하는거 아님?
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save (Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memerId);

    @Query("select m from Member m where m.detail.profile = :profile")
    Optional<Member> findByProfile(Profile profile);
}
