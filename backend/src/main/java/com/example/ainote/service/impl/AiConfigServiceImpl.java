package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ainote.dto.AiConfigRequest;
import com.example.ainote.entity.AiConfig;
import com.example.ainote.mapper.AiConfigMapper;
import com.example.ainote.service.AiConfigService;
import com.example.ainote.vo.AiConfigVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class AiConfigServiceImpl extends ServiceImpl<AiConfigMapper, AiConfig> implements AiConfigService {
    @Value("${ainote.ai.mode:real}")
    private String defaultMode;
    @Value("${ainote.ai.api-base-url:https://api.deepseek.com/v1}")
    private String defaultApiBaseUrl;
    @Value("${ainote.ai.api-key:}")
    private String defaultApiKey;
    @Value("${ainote.ai.model-name:deepseek-chat}")
    private String defaultModelName;
    @Value("${ainote.ai.mock-on-failure:true}")
    private Boolean defaultMockOnFailure;

    @Override
    public AiConfig getCurrentConfig() {
        AiConfig config = lambdaQuery().orderByDesc(AiConfig::getUpdateTime).last("limit 1").one();
        if (config != null) {
            return config;
        }
        config = new AiConfig();
        config.setProvider("DeepSeek");
        config.setMode(defaultMode);
        config.setApiBaseUrl(defaultApiBaseUrl);
        config.setApiKey(defaultApiKey);
        config.setModelName(defaultModelName);
        config.setTemperature(new BigDecimal("0.30"));
        config.setMockOnFailure(Boolean.TRUE.equals(defaultMockOnFailure) ? 1 : 0);
        config.setEnabled(1);
        config.setRemark("系统自动创建的默认 AI 配置。");
        save(config);
        return config;
    }

    @Override
    public AiConfigVO getConfigVO() {
        return AiConfigVO.from(getCurrentConfig());
    }

    @Override
    public AiConfigVO updateConfig(AiConfigRequest request) {
        AiConfig config = getCurrentConfig();
        config.setProvider(request.getProvider());
        config.setMode(request.getMode());
        config.setApiBaseUrl(request.getApiBaseUrl());
        config.setModelName(request.getModelName());
        config.setTemperature(request.getTemperature() == null ? new BigDecimal("0.30") : request.getTemperature());
        config.setMockOnFailure(Boolean.TRUE.equals(request.getMockOnFailure()) ? 1 : 0);
        config.setEnabled(request.getEnabled() == null ? 1 : request.getEnabled());
        config.setRemark(request.getRemark());
        if (StringUtils.hasText(request.getApiKey()) && !request.getApiKey().contains("****")) {
            config.setApiKey(request.getApiKey().trim());
        }
        updateById(config);
        return AiConfigVO.from(config);
    }
}
