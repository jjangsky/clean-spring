package com.jjangsky.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjangsky.splearn.application.member.provided.MemberRegister;
import com.jjangsky.splearn.domain.member.Member;
import com.jjangsky.splearn.domain.member.MemberFixture;
import com.jjangsky.splearn.domain.member.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RequiredArgsConstructor
@WebMvcTest(MemberApi.class)
class MemberApiWebMvcTest {
    @MockitoBean
    MemberRegister memberRegister;

    final MockMvcTester mvcTester;

    final ObjectMapper objectMapper;

    /**
     * API 계층에서의 테스트 목적은 해당 API가 계층을 타고 정상적으로 이동해서
     * 원하는 값이 출력이 되는가에 대해 목적을 가져야 한다.
     * -> 상세 테스는 서비스 레이어에서 테스트하기 때문
     */

    @Test
    void register() throws JsonProcessingException {
        Member member = MemberFixture.createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail() throws JsonProcessingException {

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid Email");
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

}