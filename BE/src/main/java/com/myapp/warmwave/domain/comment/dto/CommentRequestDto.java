package com.myapp.warmwave.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotNull
    @Size(min = 10, max = 500, message = "댓글은 10자 이상 1000자 이하로 입력해야 합니다.")
    private String contents;
}
