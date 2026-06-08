package com.example.ainote.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiChatVO {
    private String answer;
    private String modelName;
}
