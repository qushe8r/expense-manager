package com.qushe8r.expensemanager.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.annotation.WithMemberPrincipals;
import com.qushe8r.expensemanager.config.TestSecurityConfig;
import com.qushe8r.expensemanager.matcher.MemberDetailsMatcher;
import com.qushe8r.expensemanager.matcher.PatchPasswordMatcher;
import com.qushe8r.expensemanager.matcher.PostMemberMatcher;
import com.qushe8r.expensemanager.member.dto.PatchPassword;
import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(MemberController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureRestDocs
class MemberControllerTest {

  private static final String EMAIL_EXAMPLE = "test@email.com";

  private static final String PASSWORD_EXAMPLE = "c2f9x9@43a";

  private static final String MEMBER_DEFAULT_URL = "/members";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private MemberService memberService;

  @DisplayName("createMember(): 입력값이 유효하면 성공")
  @Test
  void createMember() throws Exception {
    // given
    Long createdMemberId = 1L;
    PostMember postMember = new PostMember(EMAIL_EXAMPLE, PASSWORD_EXAMPLE);
    String content = objectMapper.writeValueAsString(postMember);

    BDDMockito.given(memberService.createMember(Mockito.argThat(new PostMemberMatcher(postMember))))
        .willReturn(createdMemberId);

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.post(MEMBER_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.header()
                .string(HttpHeaders.LOCATION, "/members/" + createdMemberId))
        .andDo(
            MockMvcRestDocumentation.document(
                "post-members",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                HeaderDocumentation.responseHeaders(
                    HeaderDocumentation.headerWithName(HttpHeaders.LOCATION).description("리소스 위치")),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("email").description("회원 이메일"),
                    PayloadDocumentation.fieldWithPath("password").description("회원 비밀번호"))));
  }

  @DisplayName("createMemberValidationEmail(): email 유효성 검사 실패")
  @ParameterizedTest(name = "email: {0} 유효성 검사")
  @CsvSource({", null은 허용하지 않습니다.", "notEmailPattern, Email 형식이 아닙니다."})
  void createMemberValidationEmail(String email, String reason) throws Exception {
    // given
    PostMember postMember = new PostMember(email, PASSWORD_EXAMPLE);
    String content = objectMapper.writeValueAsString(postMember);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_DEFAULT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("email"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @DisplayName("createMemberValidationPassword(): password 유효성 검사 실패")
  @ParameterizedTest(name = "실패 이유: {1}")
  @CsvSource({
    "12ab#$78c, 10자 이하 문자열은 허용하지 않습니다.",
    "1234567890, 숫자로만 이루어질 수 없습니다.",
    "sdfsdxcvdw, 알파벳으로만 이루어질 수 없습니다.",
    "aaa1z$2v2t, 동일한 문자 3번 연속으로 사용할 수 없습니다.",
    "password2#, 자주 사용되는 문자열 사용할 수 없습니다."
  })
  void createMemberValidationPassword(String password, String reason) throws Exception {
    // given
    PostMember postMember = new PostMember(EMAIL_EXAMPLE, password);
    String content = objectMapper.writeValueAsString(postMember);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_DEFAULT_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[0].field").value("password"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violationErrors").isEmpty());
  }

  @WithMemberPrincipals
  @Test
  void modifyPassword() throws Exception {
    // given
    PatchPassword patchPassword = new PatchPassword("newPassword");
    String content = objectMapper.writeValueAsString(patchPassword);
    MemberDetails memberDetails = new MemberDetails(1L, "test@email.com", "");

    BDDMockito.doNothing()
        .when(memberService)
        .modifyPassword(
            Mockito.argThat(new MemberDetailsMatcher(memberDetails)),
            Mockito.argThat(new PatchPasswordMatcher(patchPassword)));

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.patch(MEMBER_DEFAULT_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(
            MockMvcRestDocumentation.document(
                "patch-members-password",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("password").description("변경할 비밀번호"))));
  }
}
