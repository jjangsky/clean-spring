package com.jjangsky.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {
    @Test
    void createMember() {
        var member = new Member("jjangsky@github.io", "youchan", "secret");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> new Member(null, "Toby", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        var member = new Member("jjangsky@github.io", "youchan", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {
        /**
         * 이미 활성화 된 회원에 대해서 다시 활성화를 시키는 경우에 대해서 대처하는 테스트 코드
         * 무의미하지 않겠냐? -> 추후 버그 가능성 농후
         */
        var member = new Member("jjangsky@github.io", "youchan", "secret");

        member.activate();

        assertThatThrownBy(() -> {
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        var member = new Member("jjangsky@github.io", "youchan", "secret");

        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        var member = new Member("jjangsky@github.io", "youchan", "secret");

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> {
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }

    /**
     * 테스트 코드 작성할 때 생각할 것.
     * 1. Given - when - then을 주석으로 정의하며 규칙을 지켜 개발하는 것도 좋지만
     *    너무 규칙을 맞출 필요는 없다. 대충 봐도 알만하게 작성해야 함 (초보들이나 bdd 씀)
     * 2. 간단한 테스트 라면 하나의 테스트 코드에 여러 가지 case를 넣어도 댐
     * 3. `@Display`로 테스트 케이스에 대한 설명을 적는데 상황을 잘 구분 해야함
     *     보통 테스트 코드는 한번 작성하고 다시 안보는데 굳이 작성해야하나?
     *     -> 실패한 Case에 대해서만 재확인 해도 되며 복합적으로 복잡한 Case 에만 추가해도 됨
     */
}