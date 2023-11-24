package com.qushe8r.expensemanager.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record PostMember(
    @Email @NotNull String email,
    @Pattern(regexp = "^.{10,}$", message = "비밀번호는 최소 10자 이상이어야 합니다.")
        @Pattern(regexp = "^(?!.*([A-Za-z0-9])\\1{2,}).*$", message = "3회 이상 연속되는 문자 사용이 불가합니다.")
        @Pattern(
            regexp =
                "^(?:(?!password|qwerty|1q2w3e4r|iloveyou|zaq1|xsw2|1qaz|2wsx|monkey|superman|asdf).)*$",
            message = "통상적으로 자주 사용되는 문자열은 사용할 수 없습니다.")
        @Pattern(
            regexp =
                "^(?=.*\\d.*[\\W_]|.*[\\W_].*\\d|.*[a-zA-Z].*\\d|.*\\d.*[a-zA-Z]|.*[a-zA-Z].*[\\W_]|.*[\\W_][a-zA-Z]).+$",
            message = "숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.")
        @NotNull String password,
    Boolean evaluationAlarm,
    Boolean recommendationAlarm) {}
