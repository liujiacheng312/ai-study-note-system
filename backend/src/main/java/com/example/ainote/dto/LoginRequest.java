package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "不能为空")
    private String username;
    @NotBlank(message = "不能为空")
    private String password;
}