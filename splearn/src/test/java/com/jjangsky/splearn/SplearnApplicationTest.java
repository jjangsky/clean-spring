package com.jjangsky.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;


class SplearnApplicationTest {

    @Test
    void run() {
        try(MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {// 정적인 요소를 목킹

            SplearnApplication.main(new String[0]);

            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
        /**
         * 정적인 요소를 Mocking 할 때는 try-with-resources 구문을 사용
         */
    }

}