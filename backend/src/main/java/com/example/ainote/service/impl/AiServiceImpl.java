package com.example.ainote.service.impl;

import com.example.ainote.common.BusinessException;
import com.example.ainote.dto.AiChatRequest;
import com.example.ainote.dto.AiSummaryRequest;
import com.example.ainote.dto.AiTagRequest;
import com.example.ainote.entity.AiChatRecord;
import com.example.ainote.entity.AiConfig;
import com.example.ainote.entity.AiUsageLog;
import com.example.ainote.mapper.AiChatRecordMapper;
import com.example.ainote.mapper.AiUsageLogMapper;
import com.example.ainote.service.AiConfigService;
import com.example.ainote.service.AiService;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.AiChatVO;
import com.example.ainote.vo.AiSummaryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    private final AiUsageLogMapper aiUsageLogMapper;
    private final AiChatRecordMapper aiChatRecordMapper;
    private final AiConfigService aiConfigService;

    @Override
    public AiSummaryVO summary(AiSummaryRequest request, Long noteId) {
        String prompt = "请为下面的学习笔记生成结构化结果，必须包含：知识点摘要、学习重点、考试重点、学习建议。"
                + "\n标题：" + request.getTitle()
                + "\n正文：" + request.getContent();
        AiConfig config = aiConfigService.getCurrentConfig();
        String real = callRealModel(config, "你是高校学习笔记整理助手，请用简体中文回答，语言清晰，适合大学生复习。", prompt);
        AiSummaryVO vo;
        if (StringUtils.hasText(real)) {
            vo = new AiSummaryVO(
                    real,
                    "已由真实大模型生成，请结合摘要内容整理自己的复习提纲。",
                    "请重点关注模型回答中的概念、流程、易错点和应用场景。",
                    "建议将 AI 输出二次加工为自己的知识清单，并结合例题复习。");
        } else {
            String plain = request.getContent().replaceAll("[#>*`\\-]", "").trim();
            String sample = plain.length() > 80 ? plain.substring(0, 80) + "..." : plain;
            vo = new AiSummaryVO(
                    "本笔记围绕“" + request.getTitle() + "”展开，核心内容包括：" + sample,
                    "1. 梳理概念定义；2. 明确业务流程；3. 结合代码或案例理解实现步骤。",
                    "重点关注概念辨析、状态流转、接口设计、数据库关系和实际应用场景。",
                    "建议先阅读摘要形成整体认识，再根据重点内容制作复习提纲，最后通过问答检查薄弱知识点。");
        }
        logUsage(noteId, "SUMMARY", prompt.length() / 2, (vo.getSummary() + vo.getKeyPoints()).length() / 2, StringUtils.hasText(real) ? "SUCCESS" : "MOCK_FALLBACK");
        return vo;
    }

    @Override
    public List<String> recommendTags(AiTagRequest request, Long noteId) {
        AiConfig config = aiConfigService.getCurrentConfig();
        String real = callRealModel(config,
                "请只返回 3 到 6 个简体中文学习标签，使用逗号分隔，不要输出解释。",
                request.getTitle() + "\n" + request.getContent());
        List<String> tags;
        if (StringUtils.hasText(real)) {
            tags = Arrays.stream(real.replace("，", ",").replace("\n", ",").split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .limit(6)
                    .toList();
        } else {
            String text = (request.getTitle() + request.getContent()).toLowerCase();
            if (text.contains("vue")) {
                tags = List.of("Vue3", "前端开发", "组件化", "TypeScript");
            } else if (text.contains("mysql") || text.contains("sql")) {
                tags = List.of("MySQL", "数据库设计", "SQL", "ER图");
            } else if (text.contains("ai") || text.contains("人工智能")) {
                tags = List.of("AI 摘要", "学习建议", "提示词", "智能问答");
            } else {
                tags = List.of("课程复习", "学习笔记", "知识整理", "重点提取");
            }
        }
        logUsage(noteId, "TAGS", request.getContent().length() / 2, tags.toString().length() / 2, StringUtils.hasText(real) ? "SUCCESS" : "MOCK_FALLBACK");
        return tags;
    }

    @Override
    public AiChatVO chat(AiChatRequest request) {
        AiConfig config = aiConfigService.getCurrentConfig();
        String prompt = (StringUtils.hasText(request.getContext()) ? request.getContext() + "\n" : "") + request.getQuestion();
        String answer = callRealModel(config, "你是智学 AI 学习笔记系统的真实 AI 问答助手。请用简体中文回答，分点说明，避免编造事实。", prompt);
        boolean realAnswer = StringUtils.hasText(answer);
        if (!realAnswer) {
            if (isRealMode(config) && !isMockOnFailure(config)) {
                throw new BusinessException("真实 AI 调用失败，请检查后台 AI API 配置中的 API Key、接口地址和模型名称");
            }
            answer = "当前未检测到可用的真实 AI API Key，系统已临时使用演示回答。针对你的问题：“"
                    + request.getQuestion()
                    + "”，建议先定位相关课程章节，再按“概念-原理-例子-易错点”四步整理。管理员在后台 AI API 配置页填写 Key 后，本页面将调用真实大模型回答。";
        }
        Long userId = SecurityUtils.getCurrentUserId();
        AiChatRecord record = new AiChatRecord();
        record.setUserId(userId);
        record.setQuestion(request.getQuestion());
        record.setAnswer(answer);
        record.setModelName(realAnswer ? config.getModelName() : "mock-ai");
        aiChatRecordMapper.insert(record);
        logUsage(null, "CHAT", prompt.length() / 2, answer.length() / 2, realAnswer ? "SUCCESS" : "MOCK_FALLBACK");
        return new AiChatVO(answer, record.getModelName());
    }

    @Override
    public Map<String, Object> studyAdvice() {
        logUsage(null, "ADVICE", 20, 60, "SUCCESS");
        return Map.of(
                "studyAdvice", "本周建议优先复习最近创建的笔记，并将 AI 摘要转化为自己的知识清单。",
                "reviewAdvice", "采用 1 天、3 天、7 天间隔复习法，对高频分类进行循环回顾。",
                "weakPoints", List.of("概念体系化", "代码实践", "考试重点归纳"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String callRealModel(AiConfig config, String systemPrompt, String userPrompt) {
        if (!isRealMode(config)
                || config.getEnabled() == null
                || config.getEnabled() != 1
                || !StringUtils.hasText(config.getApiKey())
                || !StringUtils.hasText(config.getApiBaseUrl())) {
            return null;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(config.getApiKey());
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", config.getModelName());
            body.put("messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userPrompt)));
            body.put("temperature", temperature(config));

            ResponseEntity<Map> response = new RestTemplate().postForEntity(
                    config.getApiBaseUrl().replaceAll("/$", "") + "/chat/completions",
                    new HttpEntity<>(body, headers),
                    Map.class);
            Map responseBody = response.getBody();
            if (responseBody == null) {
                return null;
            }
            List choices = (List) responseBody.get("choices");
            if (choices == null || choices.isEmpty()) {
                return null;
            }
            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");
            Object content = message == null ? null : message.get("content");
            return content == null ? null : String.valueOf(content);
        } catch (Exception ex) {
            log.warn("Real AI request failed: {}", ex.getMessage());
            return null;
        }
    }

    private boolean isRealMode(AiConfig config) {
        return config != null && "real".equalsIgnoreCase(config.getMode());
    }

    private boolean isMockOnFailure(AiConfig config) {
        return config == null || config.getMockOnFailure() == null || config.getMockOnFailure() == 1;
    }

    private BigDecimal temperature(AiConfig config) {
        return config.getTemperature() == null ? new BigDecimal("0.30") : config.getTemperature();
    }

    private void logUsage(Long noteId, String aiType, int inputTokens, int outputTokens, String status) {
        AiUsageLog log = new AiUsageLog();
        log.setUserId(SecurityUtils.getCurrentUserId());
        log.setNoteId(noteId);
        log.setAiType(aiType);
        log.setInputTokens(Math.max(inputTokens, 1));
        log.setOutputTokens(Math.max(outputTokens, 1));
        log.setStatus(status);
        aiUsageLogMapper.insert(log);
    }
}
