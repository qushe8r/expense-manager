package com.qushe8r.expensemanager.member.controller;

import com.qushe8r.expensemanager.common.utils.UriCreator;
import com.qushe8r.expensemanager.member.dto.PatchPassword;
import com.qushe8r.expensemanager.member.dto.PostMember;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.member.service.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private static final String MEMBER_DEFAULT_URL = "/members";

  private final MemberService memberService;

  @PostMapping
  public ResponseEntity<Void> createMember(@Validated @RequestBody PostMember dto) {
    Long memberId = memberService.createMember(dto);
    URI uri = UriCreator.createUri(MEMBER_DEFAULT_URL, memberId);
    return ResponseEntity.created(uri).build();
  }

  @PatchMapping("/password")
  public ResponseEntity<Void> modifyPassword(
      @AuthenticationPrincipal MemberDetails memberDetails, @RequestBody PatchPassword dto) {
    memberService.modifyPassword(memberDetails, dto);
    return ResponseEntity.ok().build();
  }
}
