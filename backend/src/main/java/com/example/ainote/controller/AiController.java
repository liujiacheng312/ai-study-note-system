package com.example.ainote.controller;

import com.example.ainote.common.Result;
import com.example.ainote.dto.AiChatRequest;
import com.example.ainote.dto.AiConfigRequest;
import com.example.ainote.dto.AiSummaryRequest;
import com.example.ainote.dto.AiTagRequest;
import com.example.ainote.service.AiConfigService;
import com.example.ainote.service.AiService;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.AiChatVO;
import com.example.ainote.vo.AiConfigVO;
import com.example.ainote.vo.AiSummaryVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;
    private final AiConfigService aiConfigService;

    @PostMapping("/summary")
    public Result<AiSummaryVO> summary(@Valid @RequestBody AiSummaryRequest request) {
        return Result.success(aiService.summary(request, null));
    }

    @PostMapping("/tags")
    public Result<List<String>> tags(@Valid @RequestBody AiTagRequest request) {
        return Result.success(aiService.recommendTags(request, null));
    }

    @PostMapping("/chat")
    public Result<AiChatVO> chat(@Valid @RequestBody AiChatRequest request) {
        return Result.success(aiService.chat(request));
    }

    @GetMapping("/config")
    public Result<AiConfigVO> config() {
        return Result.success(aiConfigService.getUserConfigVO(SecurityUtils.getCurrentUserId()));
    }

    @PutMapping("/config")
    public Result<AiConfigVO> updateConfig(@Valid @RequestBody AiConfigRequest request) {
        return Result.success(aiConfigService.updateUserConfig(SecurityUtils.getCurrentUserId(), request));
    }

    @GetMapping("/study-advice")
    public Result<Map<String, Object>> studyAdvice() {
        return Result.success(aiService.studyAdvice());
    }
}
