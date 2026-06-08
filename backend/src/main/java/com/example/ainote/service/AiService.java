package com.example.ainote.service;

import com.example.ainote.dto.AiChatRequest;
import com.example.ainote.dto.AiSummaryRequest;
import com.example.ainote.dto.AiTagRequest;
import com.example.ainote.vo.AiChatVO;
import com.example.ainote.vo.AiSummaryVO;

import java.util.List;
import java.util.Map;

public interface AiService {
    AiSummaryVO summary(AiSummaryRequest request, Long noteId);

    List<String> recommendTags(AiTagRequest request, Long noteId);

    AiChatVO chat(AiChatRequest request);

    Map<String, Object> studyAdvice();
}
