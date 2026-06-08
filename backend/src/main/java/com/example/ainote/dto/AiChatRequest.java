package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AiChatRequest {
    @NotBlank(message = "不能为空")
    private String question;
    private String context;
}