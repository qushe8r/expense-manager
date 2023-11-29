package com.qushe8r.expensemanager.security.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.qushe8r.expensemanager.annotation.WebMvcTestWithoutSecurityConfig;
import com.qushe8r.expensemanager.security.service.AuthService;
import com.qushe8r.expensemanager.security.utils.CookieCreator;
import com.qushe8r.expensemanager.stub.JwtFactory;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.cookies.CookieDocumentation;
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

@WebMvcTestWithoutSecurityConfig(AuthController.class)
@AutoConfigureRestDocs
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @Test
  void reissue() throws Exception {
    // given
    String token = JwtFactory.withDefaultValues().generateStubToken();
    Cookie cookie = CookieCreator.createCookie(token, "domain", 50);
    ResponseCookie responseCookie =
        CookieCreator.createResponseCookie("newRefreshToken", "domain", 500);

    HttpHeaders httpHeaders = new HttpHeaders();
    String newAccessToken = "newAccessToken";
    httpHeaders.add(HttpHeaders.AUTHORIZATION, newAccessToken);
    httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

    BDDMockito.given(authService.reissue(token)).willReturn(httpHeaders);

    // when
    ResultActions actions =
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/reissue")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.AUTHORIZATION, newAccessToken))
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.SET_COOKIE))
        .andDo(MockMvcRestDocumentation.document(
            "post-reissue",
            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
            CookieDocumentation.requestCookies(
                CookieDocumentation.cookieWithName("Refresh").description("리프레시 토큰")
            ),
            HeaderDocumentation.responseHeaders(
                HeaderDocumentation.headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰(재발급)")
            ),
            CookieDocumentation.responseCookies(
                CookieDocumentation.cookieWithName("Refresh").description("리프레시 토큰(재발급)")
            )
        ));
  }
}
