package com.example.ainote.controller;

import com.example.ainote.common.PageResult;
import com.example.ainote.common.Result;
import com.example.ainote.service.FavoriteBizService;
import com.example.ainote.vo.NoteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteBizService favoriteBizService;

    @PostMapping("/{noteId}")
    public Result<Void> add(@PathVariable Long noteId) {
        favoriteBizService.add(noteId);
        return Result.success();
    }

    @DeleteMapping("/{noteId}")
    public Result<Void> remove(@PathVariable Long noteId) {
        favoriteBizService.remove(noteId);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<PageResult<NoteVO>> my(@RequestParam(defaultValue = "1") Long pageNo,
                                         @RequestParam(defaultValue = "10") Long pageSize) {
        return Result.success(favoriteBizService.myPage(pageNo, pageSize));
    }

    @GetMapping("/check/{noteId}")
    public Result<Boolean> check(@PathVariable Long noteId) {
        return Result.success(favoriteBizService.check(noteId));
    }
}
