package com.example.ainote.controller;

import com.example.ainote.common.PageResult;
import com.example.ainote.common.Result;
import com.example.ainote.dto.NoteQueryRequest;
import com.example.ainote.dto.NoteRequest;
import com.example.ainote.service.NoteService;
import com.example.ainote.vo.NoteVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public Result<NoteVO> create(@Valid @RequestBody NoteRequest request) {
        return Result.success(noteService.create(request));
    }

    @PutMapping("/{id}")
    public Result<NoteVO> update(@PathVariable Long id, @Valid @RequestBody NoteRequest request) {
        return Result.success(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noteService.deleteNote(id);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<PageResult<NoteVO>> myPage(NoteQueryRequest query) {
        return Result.success(noteService.myPage(query));
    }

    @GetMapping("/public")
    public Result<PageResult<NoteVO>> publicPage(NoteQueryRequest query) {
        return Result.success(noteService.publicPage(query));
    }

    @GetMapping("/{id}")
    public Result<NoteVO> detail(@PathVariable Long id) {
        return Result.success(noteService.detail(id));
    }

    @PutMapping("/{id}/submit-ai")
    public Result<NoteVO> submitAi(@PathVariable Long id) {
        return Result.success(noteService.submitAi(id));
    }

    @PutMapping("/{id}/publish")
    public Result<NoteVO> publish(@PathVariable Long id) {
        return Result.success(noteService.publish(id));
    }

    @PutMapping("/{id}/archive")
    public Result<NoteVO> archive(@PathVariable Long id) {
        return Result.success(noteService.archive(id));
    }
}
