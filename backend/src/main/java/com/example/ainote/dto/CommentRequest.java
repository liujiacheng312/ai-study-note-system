package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "不能为空")
    private Long noteId;
    private Long parentId;
    @NotBlank(message = "不能为空")
    private String content;
}