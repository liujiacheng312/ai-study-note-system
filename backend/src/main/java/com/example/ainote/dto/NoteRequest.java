package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class NoteRequest {
    private Long categoryId;
    @NotBlank(message = "不能为空")
    private String title;
    @NotBlank(message = "不能为空")
    private String content;
    private List<Long> tagIds;
}
