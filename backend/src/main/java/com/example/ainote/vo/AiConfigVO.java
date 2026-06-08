package com.example.ainote.vo;

import com.example.ainote.entity.AiConfig;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Data
public class AiConfigVO {
    private Long id;
    private String provider;
    private String mode;
    private String apiBaseUrl;
    private String maskedApiKey;
    private Boolean apiKeyConfigured;
    private String modelName;
    private BigDecimal temperature;
    private Boolean mockOnFailure;
    private Integer enabled;
    private String remark;

    public static AiConfigVO from(AiConfig config) {
        AiConfigVO vo = new AiConfigVO();
        vo.setId(config.getId());
        vo.setProvider(config.getProvider());
        vo.setMode(config.getMode());
        vo.setApiBaseUrl(config.getApiBaseUrl());
        vo.setApiKeyConfigured(StringUtils.hasText(config.getApiKey()));
        vo.setMaskedApiKey(mask(config.getApiKey()));
        vo.setModelName(config.getModelName());
        vo.setTemperature(config.getTemperature());
        vo.setMockOnFailure(config.getMockOnFailure() != null && config.getMockOnFailure() == 1);
        vo.setEnabled(config.getEnabled());
        vo.setRemark(config.getRemark());
        return vo;
    }

    private static String mask(String key) {
        if (!StringUtils.hasText(key)) {
            return "";
        }
        if (key.length() <= 8) {
            return "********";
        }
        return key.substring(0, 4) + "****" + key.substring(key.length() - 4);
    }
}
