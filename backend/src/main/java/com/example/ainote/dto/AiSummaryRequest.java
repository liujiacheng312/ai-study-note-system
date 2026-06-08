package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSummaryRequest {
    @NotBlank(message = "不能为空")
    private String title;
    @NotBlank(message = "不能为空")
    private String content;
}
