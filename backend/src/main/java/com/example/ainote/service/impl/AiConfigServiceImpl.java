package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ainote.dto.AiConfigRequest;
import com.example.ainote.entity.AiConfig;
import com.example.ainote.mapper.AiConfigMapper;
import com.example.ainote.service.AiConfigService;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.AiConfigVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class AiConfigServiceImpl extends ServiceImpl<AiConfigMapper, AiConfig> implements AiConfigService {
    private static final String SCOPE_GLOBAL = "GLOBAL";
    private static final String SCOPE_USER = "USER";
    private static final long GLOBAL_USER_ID = 0L;

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
        try {
            return getEffectiveConfig(SecurityUtils.getCurrentUserId());
        } catch (Exception ex) {
            return getGlobalConfig();
        }
    }

    @Override
    public AiConfig getEffectiveConfig(Long userId) {
        if (userId != null) {
            AiConfig userConfig = findUserConfig(userId);
            if (isUsableUserConfig(userConfig)) {
                return userConfig;
            }
        }
        return getGlobalConfig();
    }

    @Override
    public AiConfigVO getGlobalConfigVO() {
        return AiConfigVO.from(getGlobalConfig(), false);
    }

    @Override
    public AiConfigVO updateGlobalConfig(AiConfigRequest request) {
        AiConfig config = getGlobalConfig();
        applyRequest(config, request);
        config.setScope(SCOPE_GLOBAL);
        config.setUserId(GLOBAL_USER_ID);
        updateById(config);
        return AiConfigVO.from(config, false);
    }

    @Override
    public AiConfigVO getUserConfigVO(Long userId) {
        AiConfig config = findUserConfig(userId);
        if (config != null) {
            return AiConfigVO.from(config, !isUsableUserConfig(config));
        }
        AiConfig global = getGlobalConfig();
        AiConfigVO vo = AiConfigVO.from(global, true);
        vo.setId(null);
        vo.setScope(SCOPE_USER);
        vo.setUserId(userId);
        vo.setMaskedApiKey("");
        vo.setApiKeyConfigured(false);
        vo.setEnabled(0);
        vo.setRemark("");
        return vo;
    }

    @Override
    public AiConfigVO updateUserConfig(Long userId, AiConfigRequest request) {
        AiConfig config = findUserConfig(userId);
        boolean created = false;
        if (config == null) {
            config = new AiConfig();
            config.setScope(SCOPE_USER);
            config.setUserId(userId);
            created = true;
        }
        applyRequest(config, request);
        config.setScope(SCOPE_USER);
        config.setUserId(userId);
        if (created) {
            save(config);
        } else {
            updateById(config);
        }
        return AiConfigVO.from(config, !isUsableUserConfig(config));
    }

    private AiConfig getGlobalConfig() {
        AiConfig config = lambdaQuery()
                .eq(AiConfig::getScope, SCOPE_GLOBAL)
                .eq(AiConfig::getUserId, GLOBAL_USER_ID)
                .last("limit 1")
                .one();
        if (config != null) {
            return config;
        }
        config = new AiConfig();
        config.setScope(SCOPE_GLOBAL);
        config.setUserId(GLOBAL_USER_ID);
        config.setProvider("DeepSeek");
        config.setMode(defaultMode);
        config.setApiBaseUrl(defaultApiBaseUrl);
        config.setApiKey(defaultApiKey);
        config.setModelName(defaultModelName);
        config.setTemperature(new BigDecimal("0.30"));
        config.setMockOnFailure(Boolean.TRUE.equals(defaultMockOnFailure) ? 1 : 0);
        config.setEnabled(1);
        config.setRemark("Default global AI config.");
        save(config);
        return config;
    }

    private AiConfig findUserConfig(Long userId) {
        if (userId == null) {
            return null;
        }
        return lambdaQuery()
                .eq(AiConfig::getScope, SCOPE_USER)
                .eq(AiConfig::getUserId, userId)
                .last("limit 1")
                .one();
    }

    private boolean isUsableUserConfig(AiConfig config) {
        if (config == null || config.getEnabled() == null || config.getEnabled() != 1) {
            return false;
        }
        if ("mock".equalsIgnoreCase(config.getMode())) {
            return true;
        }
        return StringUtils.hasText(config.getApiKey());
    }

    private void applyRequest(AiConfig config, AiConfigRequest request) {
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
    }
}
