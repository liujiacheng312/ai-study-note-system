package com.example.ainote.controller;

import com.example.ainote.common.Result;
import com.example.ainote.service.StatisticsService;
import com.example.ainote.vo.StatisticsOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    public Result<StatisticsOverviewVO> overview() {
        return Result.success(statisticsService.overview(false));
    }

    @GetMapping("/category")
    public Result<List<Map<String, Object>>> category() {
        return Result.success(statisticsService.category());
    }

    @GetMapping("/monthly")
    public Result<List<Map<String, Object>>> monthly() {
        return Result.success(statisticsService.monthly());
    }
}
