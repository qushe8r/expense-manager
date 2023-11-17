package com.qushe8r.expensemanager.integration.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.member.dto.PostMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerIntegrationTest {

  private static final String EMAIL_EXAMPLE = "test@email.com";

  private static final String PASSWORD_EXAMPLE = "c2f9x9@43a";

  private static final String MEMBER_DEFAULT_URL = "/members";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void createMember() throws Exception {
    // given
    PostMember postMember = new PostMember(EMAIL_EXAMPLE, PASSWORD_EXAMPLE);
    String content = objectMapper.writeValueAsString(postMember);

    // when
    ResultActions actions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_DEFAULT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content));

    // then
    actions
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION));
  }
}
