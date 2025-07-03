package com.jjangsky.splearn.domain;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@MappedSuperclass
@ToString(callSuper = true) // ToString 어노테이션을 사용하여 상위 클래스의 toString 메소드도 호출하도록 설정
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(onMethod_ = {@Nullable})
    // ID 필드 같은 경우는 모든 엔티티에 존재하며 공통 Class로 묶어서 상속 시키는 것이 좋다.
    private Long id;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
        /**
         * identity 전략을 사용하고 있는 경우 DB에 저장하기 전에는 id 값이 null로 저장되어 있는데
         * 이 과정에서 비즈니스 로직을 처리하게 되면 오류를 접할 수 있어 null 체크를 해줘야 한다.
         */

    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    /**
     * 프록시 오브젝트가 DB 값을 읽어오지 않고 ID 값만 갖고 비교를 하고 있을 때
     * Equals 메소드를 사용하여 동등성을 비교하는 과정에서 문제가 생길 수 있다.
     * (하이버네이트 프록시가 적용되어 있을 경우 정확하게 비교하기 어려움)
     * -> 두 개의 엔티티 클래스는 사실 같은 객체이다 를 증명해야함
     *
     */

}
