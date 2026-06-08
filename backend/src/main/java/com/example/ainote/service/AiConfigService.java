package com.example.ainote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ainote.dto.AiConfigRequest;
import com.example.ainote.entity.AiConfig;
import com.example.ainote.vo.AiConfigVO;

public interface AiConfigService extends IService<AiConfig> {
    AiConfig getCurrentConfig();

    AiConfig getEffectiveConfig(Long userId);

    AiConfigVO getGlobalConfigVO();

    AiConfigVO updateGlobalConfig(AiConfigRequest request);

    AiConfigVO getUserConfigVO(Long userId);

    AiConfigVO updateUserConfig(Long userId, AiConfigRequest request);
}
