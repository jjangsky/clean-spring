package com.jjangsky.splearn;

import com.jjangsky.splearn.domain.member.MemberRegisterRequest;
import org.assertj.core.api.AssertProvider;
import org.springframework.test.json.JsonPathValueAssert;

import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AssertThatUtils {
    public static Consumer<AssertProvider<JsonPathValueAssert>> notNull() {
        return value -> assertThat(value)
                .isNotNull();
    }
    public static Consumer<AssertProvider<JsonPathValueAssert>> equalTo(MemberRegisterRequest expected) {
        return value -> assertThat(value)
                .isEqualTo(expected);
    }
}
