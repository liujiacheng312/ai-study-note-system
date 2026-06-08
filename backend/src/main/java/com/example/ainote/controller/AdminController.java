package com.example.ainote.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ainote.common.PageResult;
import com.example.ainote.common.Result;
import com.example.ainote.dto.AnnouncementRequest;
import com.example.ainote.dto.CategoryRequest;
import com.example.ainote.dto.TagRequest;
import com.example.ainote.entity.Announcement;
import com.example.ainote.entity.Category;
import com.example.ainote.entity.Comment;
import com.example.ainote.entity.Note;
import com.example.ainote.entity.Tag;
import com.example.ainote.entity.SysUser;
import com.example.ainote.service.AnnouncementService;
import com.example.ainote.service.CategoryService;
import com.example.ainote.service.CommentService;
import com.example.ainote.service.NoteService;
import com.example.ainote.service.StatisticsService;
import com.example.ainote.service.SysUserService;
import com.example.ainote.service.TagService;
import com.example.ainote.vo.StatisticsOverviewVO;
import com.example.ainote.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final SysUserService sysUserService;
    private final NoteService noteService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final AnnouncementService announcementService;
    private final CommentService commentService;
    private final StatisticsService statisticsService;

    @GetMapping("/user/page")
    public Result<PageResult<UserVO>> userPage(@RequestParam(defaultValue = "1") Long pageNo,
                                               @RequestParam(defaultValue = "10") Long pageSize,
                                               @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword);
        }
        IPage<UserVO> page = sysUserService.page(new Page<>(pageNo, pageSize), wrapper.orderByDesc(SysUser::getCreateTime)).convert(UserVO::from);
        return Result.success(PageResult.from(page));
    }

    @PutMapping("/user/{id}/disable")
    public Result<Void> disableUser(@PathVariable Long id) {
        sysUserService.changeStatus(id, 0);
        return Result.success();
    }

    @PutMapping("/user/{id}/enable")
    public Result<Void> enableUser(@PathVariable Long id) {
        sysUserService.changeStatus(id, 1);
        return Result.success();
    }

    @PutMapping("/user/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        sysUserService.resetPassword(id);
        return Result.success();
    }

    @GetMapping("/notes/page")
    public Result<PageResult<Note>> notePage(@RequestParam(defaultValue = "1") Long pageNo,
                                             @RequestParam(defaultValue = "10") Long pageSize) {
        return Result.success(PageResult.from(noteService.page(new Page<>(pageNo, pageSize), new LambdaQueryWrapper<Note>().orderByDesc(Note::getCreateTime))));
    }

    @DeleteMapping("/notes/{id}")
    public Result<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return Result.success();
    }

    @GetMapping("/categories")
    public Result<List<Category>> categories() {
        return Result.success(categoryService.list());
    }

    @PostMapping("/categories")
    public Result<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        categoryService.save(category);
        return Result.success(category);
    }

    @PutMapping("/categories/{id}")
    public Result<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.getById(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder());
        categoryService.updateById(category);
        return Result.success(category);
    }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success();
    }

    @GetMapping("/tags")
    public Result<List<Tag>> tags() {
        return Result.success(tagService.list());
    }

    @PostMapping("/tags")
    public Result<Tag> createTag(@Valid @RequestBody TagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tag.setUseCount(0);
        tagService.save(tag);
        return Result.success(tag);
    }

    @PutMapping("/tags/{id}")
    public Result<Tag> updateTag(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        Tag tag = tagService.getById(id);
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tagService.updateById(tag);
        return Result.success(tag);
    }

    @DeleteMapping("/tags/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.success();
    }

    @GetMapping("/announcements")
    public Result<List<Announcement>> announcements() {
        return Result.success(announcementService.list(new LambdaQueryWrapper<Announcement>().orderByDesc(Announcement::getCreateTime)));
    }

    @PostMapping("/announcements")
    public Result<Announcement> createAnnouncement(@Valid @RequestBody AnnouncementRequest request) {
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        announcementService.save(announcement);
        return Result.success(announcement);
    }

    @PutMapping("/announcements/{id}")
    public Result<Announcement> updateAnnouncement(@PathVariable Long id, @Valid @RequestBody AnnouncementRequest request) {
        Announcement announcement = announcementService.getById(id);
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setStatus(request.getStatus());
        announcementService.updateById(announcement);
        return Result.success(announcement);
    }

    @DeleteMapping("/announcements/{id}")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.removeById(id);
        return Result.success();
    }

    @GetMapping("/comments/page")
    public Result<PageResult<Comment>> comments(@RequestParam(defaultValue = "1") Long pageNo,
                                                @RequestParam(defaultValue = "10") Long pageSize) {
        return Result.success(PageResult.from(commentService.page(new Page<>(pageNo, pageSize), new LambdaQueryWrapper<Comment>().orderByDesc(Comment::getCreateTime))));
    }

    @PutMapping("/comments/{id}/audit")
    public Result<Void> auditComment(@PathVariable Long id, @RequestParam Integer status) {
        Comment comment = commentService.getById(id);
        comment.setAuditStatus(status);
        commentService.updateById(comment);
        return Result.success();
    }

    @GetMapping("/statistics/overview")
    public Result<StatisticsOverviewVO> adminOverview() {
        return Result.success(statisticsService.overview(true));
    }
}
