package com.jjangsky.splearn.domain;

public interface PasswordEncoder {
    String encode(String password);
    boolean matches(String password, String passwordHash);

    /**
     * 따로 도메인으로 만들지 않고 인터페이스로 선언
     * 이러한 외부 서비스를 주입할 때 생성자를 쓰는 것도 좋지만
     *
     * 정적 팩토리 메소드 즉, 스태틱 팩토리 메소드를 사용하는 것도 좋은 방법이다.
     * -> 생성자의 이름을 내 마음대로 지정할 수 있음
     */
}
