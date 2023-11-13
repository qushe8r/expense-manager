package com.qushe8r.expensemanager.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PostCategory(
    @NotNull
        @Pattern(regexp = "^[가-힣]*$", message = "한글만 사용할 수 있습니다.")
        @Size(min = 1, max = 10, message = "1글자 이상, 10글자 이하여야 합니다.")
        String name) {}
