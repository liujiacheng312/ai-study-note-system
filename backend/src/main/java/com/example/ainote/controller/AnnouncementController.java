package com.example.ainote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ainote.common.Result;
import com.example.ainote.entity.Announcement;
import com.example.ainote.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @GetMapping
    public Result<List<Announcement>> list() {
        return Result.success(announcementService.list(new LambdaQueryWrapper<Announcement>().eq(Announcement::getStatus, 1).orderByDesc(Announcement::getCreateTime)));
    }
}
