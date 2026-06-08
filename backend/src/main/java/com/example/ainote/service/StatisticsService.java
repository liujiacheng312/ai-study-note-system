package com.example.ainote.service;

import com.example.ainote.vo.StatisticsOverviewVO;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    StatisticsOverviewVO overview(boolean admin);

    List<Map<String, Object>> category();

    List<Map<String, Object>> monthly();
}
