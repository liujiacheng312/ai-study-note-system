package com.example.ainote.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSummaryVO {
    private String summary;
    private String keyPoints;
    private String examPoints;
    private String suggestion;
}
