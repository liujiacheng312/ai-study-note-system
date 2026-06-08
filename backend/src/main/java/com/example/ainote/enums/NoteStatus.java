package com.example.ainote.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoteStatus {
    DRAFT("草稿"),
WAITING_AI("等待AI处理"),
AI_FINISHED("AI处理完成"),
PUBLISHED("已发布"),
ARCHIVED("已归档"),
DELETED("已删除");

    private final String label;
}
