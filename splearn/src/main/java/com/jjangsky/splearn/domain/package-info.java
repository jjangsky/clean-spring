@NonNullApi
package com.jjangsky.splearn.domain;

import org.springframework.lang.NonNullApi;

/**
 * [Memo]
 * spotbug 플러그인 추가
 *
 * String의 경우 null값을 허용하여 null을 의도한 경우가 아닌
 * 의도치 않은 상황에서 null을 넣어도 빌드가 되고 배포가 되어버린다.
 * spotbug 플러그인을 사용하여 메소드의 파라미터의 `@Nonnull` 을 추가하여 빌드 시점에 에러를 체크할 수 있다.
 * -> 어노테이션이 없다면 오류로 취급하지 않음
 *
 * 패키지 단위로 설정하여 역으로 null을 허용할 필드에 대해서 Nullable 처리를 하는 것이 안전하다.
 */