package com.example.ainote.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsOverviewVO {
    private Long noteTotal;
    private Long weekNewNotes;
    private Long favoriteTotal;
    private Long aiUsageTotal;
    private Long userTotal;
}
