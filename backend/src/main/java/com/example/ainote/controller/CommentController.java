package com.example.ainote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ainote.common.BusinessException;
import com.example.ainote.common.Result;
import com.example.ainote.dto.CommentRequest;
import com.example.ainote.entity.Comment;
import com.example.ainote.entity.Note;
import com.example.ainote.enums.NoteStatus;
import com.example.ainote.mapper.NoteMapper;
import com.example.ainote.service.CommentService;
import com.example.ainote.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final NoteMapper noteMapper;

    @PostMapping
    public Result<Comment> create(@Valid @RequestBody CommentRequest request) {
        Note note = noteMapper.selectById(request.getNoteId());
        if (note == null || !NoteStatus.PUBLISHED.name().equals(note.getStatus())) {
            throw new BusinessException("只能评论已发布笔记");
        }
        Comment comment = new Comment();
        comment.setNoteId(request.getNoteId());
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        comment.setUserId(SecurityUtils.getCurrentUserId());
        comment.setAuditStatus(1);
        commentService.save(comment);
        note.setCommentCount((note.getCommentCount() == null ? 0 : note.getCommentCount()) + 1);
        noteMapper.updateById(note);
        return Result.success(comment);
    }

    @GetMapping("/note/{noteId}")
    public Result<List<Comment>> noteComments(@PathVariable Long noteId) {
        return Result.success(commentService.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getNoteId, noteId)
                .eq(Comment::getAuditStatus, 1)
                .orderByAsc(Comment::getCreateTime)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return Result.success();
        }
        if (!SecurityUtils.isAdmin() && !comment.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BusinessException("不能删除他人的评论");
        }
        commentService.removeById(id);
        return Result.success();
    }
}
