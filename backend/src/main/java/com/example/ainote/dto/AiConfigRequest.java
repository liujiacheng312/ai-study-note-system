package com.example.ainote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AiConfigRequest {
    @NotBlank(message = "不能为空")
    private String provider;
    @NotBlank(message = "不能为空")
    private String mode;
    @NotBlank(message = "不能为空")
    private String apiBaseUrl;
    private String apiKey;
    @NotBlank(message = "不能为空")
    private String modelName;
    private BigDecimal temperature;
    private Boolean mockOnFailure;
    private Integer enabled;
    private String remark;
}
