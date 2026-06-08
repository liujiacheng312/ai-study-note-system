package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnnouncementRequest {
    @NotBlank(message = "不能为空")
    private String title;
    @NotBlank(message = "不能为空")
    private String content;
    private Integer status;
}