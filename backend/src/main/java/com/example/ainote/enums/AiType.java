package com.example.ainote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AiType {
    SUMMARY("AI摘要"),
TAGS("AI标签推荐"),
CHAT("AI问答"),
ADVICE("学习建议");

    private final String label;
}
