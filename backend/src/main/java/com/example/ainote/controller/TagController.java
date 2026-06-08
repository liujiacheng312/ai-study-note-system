package com.example.ainote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ainote.common.Result;
import com.example.ainote.entity.Tag;
import com.example.ainote.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public Result<List<Tag>> list() {
        return Result.success(tagService.list());
    }

    @GetMapping("/hot")
    public Result<List<Tag>> hot() {
        return Result.success(tagService.list(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getUseCount).last("limit 10")));
    }
}
