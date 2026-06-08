package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ainote.common.BusinessException;
import com.example.ainote.common.PageResult;
import com.example.ainote.dto.AiSummaryRequest;
import com.example.ainote.dto.NoteQueryRequest;
import com.example.ainote.dto.NoteRequest;
import com.example.ainote.entity.Category;
import com.example.ainote.entity.Note;
import com.example.ainote.entity.NoteTag;
import com.example.ainote.entity.Tag;
import com.example.ainote.enums.NoteStatus;
import com.example.ainote.mapper.CategoryMapper;
import com.example.ainote.mapper.NoteMapper;
import com.example.ainote.mapper.NoteTagMapper;
import com.example.ainote.mapper.TagMapper;
import com.example.ainote.service.AiService;
import com.example.ainote.service.NoteService;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.AiSummaryVO;
import com.example.ainote.vo.NoteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {
    private final NoteTagMapper noteTagMapper;
    private final TagMapper tagMapper;
    private final CategoryMapper categoryMapper;
    private final AiService aiService;

    @Override
    @Transactional
    public NoteVO create(NoteRequest request) {
        Note note = new Note();
        note.setUserId(SecurityUtils.getCurrentUserId());
        note.setCategoryId(request.getCategoryId());
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setStatus(NoteStatus.DRAFT.name());
        note.setViewCount(0);
        note.setLikeCount(0);
        note.setFavoriteCount(0);
        note.setCommentCount(0);
        save(note);
        saveTags(note.getId(), request.getTagIds());
        return toVO(note);
    }

    @Override
    @Transactional
    public NoteVO updateNote(Long id, NoteRequest request) {
        Note note = requireOwnerEditable(id);
        note.setCategoryId(request.getCategoryId());
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        updateById(note);
        saveTags(note.getId(), request.getTagIds());
        return toVO(getById(id));
    }

    @Override
    public void deleteNote(Long id) {
        Note note = getById(id);
        if (note == null) {
            throw new BusinessException("笔记不存在");
        }
        if (!SecurityUtils.isAdmin() && !note.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BusinessException("不能删除他人的笔记");
        }
        note.setStatus(NoteStatus.DELETED.name());
        updateById(note);
        removeById(id);
    }

    @Override
    public NoteVO detail(Long id) {
        Note note = getById(id);
        if (note == null) {
            throw new BusinessException("笔记不存在");
        }
        boolean own = !SecurityUtils.isAdmin() && safeCurrentUserId().equals(note.getUserId());
        boolean published = NoteStatus.PUBLISHED.name().equals(note.getStatus());
        if (!published && !own && !SecurityUtils.isAdmin()) {
            throw new BusinessException("无权查看该笔记");
        }
        if (published) {
            note.setViewCount(nullToZero(note.getViewCount()) + 1);
            updateById(note);
        }
        return toVO(note);
    }

    @Override
    public PageResult<NoteVO> myPage(NoteQueryRequest query) {
        Long userId = SecurityUtils.getCurrentUserId();
        LambdaQueryWrapper<Note> wrapper = baseWrapper(query)
                .eq(Note::getUserId, userId)
                .ne(Note::getStatus, NoteStatus.DELETED.name())
                .orderByDesc(Note::getUpdateTime);
        return PageResult.from(page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(this::toVO));
    }

    @Override
    public PageResult<NoteVO> publicPage(NoteQueryRequest query) {
        LambdaQueryWrapper<Note> wrapper = baseWrapper(query)
                .eq(Note::getStatus, NoteStatus.PUBLISHED.name())
                .orderByDesc(Note::getUpdateTime);
        return PageResult.from(page(new Page<>(query.getPageNo(), query.getPageSize()), wrapper).convert(this::toVO));
    }

    @Override
    @Transactional
    public NoteVO submitAi(Long id) {
        Note note = requireOwner(id);
        if (!NoteStatus.DRAFT.name().equals(note.getStatus()) && !NoteStatus.AI_FINISHED.name().equals(note.getStatus())) {
            throw new BusinessException("当前状态不能提交 AI 处理");
        }
        note.setStatus(NoteStatus.WAITING_AI.name());
        updateById(note);
        AiSummaryVO summary = aiService.summary(new AiSummaryRequest(note.getTitle(), note.getContent()), note.getId());
        note.setSummary(summary.getSummary());
        note.setAiKeyPoints(summary.getKeyPoints());
        note.setAiExamPoints(summary.getExamPoints());
        note.setAiSuggestion(summary.getSuggestion());
        note.setStatus(NoteStatus.AI_FINISHED.name());
        updateById(note);
        return toVO(note);
    }

    @Override
    public NoteVO publish(Long id) {
        Note note = requireOwner(id);
        if (!NoteStatus.AI_FINISHED.name().equals(note.getStatus())) {
            throw new BusinessException("请先完成 AI 处理后再发布");
        }
        note.setStatus(NoteStatus.PUBLISHED.name());
        updateById(note);
        return toVO(note);
    }

    @Override
    public NoteVO archive(Long id) {
        Note note = requireOwner(id);
        if (!NoteStatus.PUBLISHED.name().equals(note.getStatus())) {
            throw new BusinessException("只有已发布笔记可以归档");
        }
        note.setStatus(NoteStatus.ARCHIVED.name());
        updateById(note);
        return toVO(note);
    }

    private LambdaQueryWrapper<Note> baseWrapper(NoteQueryRequest query) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(Note::getTitle, query.getKeyword()).or().like(Note::getContent, query.getKeyword()));
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(Note::getCategoryId, query.getCategoryId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(Note::getStatus, query.getStatus());
        }
        if (query.getTagId() != null) {
            List<Long> noteIds = noteTagMapper.selectList(new LambdaQueryWrapper<NoteTag>().eq(NoteTag::getTagId, query.getTagId()))
                    .stream().map(NoteTag::getNoteId).toList();
            wrapper.in(!noteIds.isEmpty(), Note::getId, noteIds);
            if (noteIds.isEmpty()) {
                wrapper.eq(Note::getId, -1L);
            }
        }
        return wrapper;
    }

    private Note requireOwnerEditable(Long id) {
        Note note = requireOwner(id);
        if (!NoteStatus.DRAFT.name().equals(note.getStatus()) && !NoteStatus.AI_FINISHED.name().equals(note.getStatus())) {
            throw new BusinessException("当前状态不允许编辑");
        }
        return note;
    }

    private Note requireOwner(Long id) {
        Note note = getById(id);
        if (note == null) {
            throw new BusinessException("笔记不存在");
        }
        if (!SecurityUtils.isAdmin() && !note.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new BusinessException("不能操作他人的笔记");
        }
        return note;
    }

    private Long safeCurrentUserId() {
        try {
            return SecurityUtils.getCurrentUserId();
        } catch (Exception ex) {
            return -1L;
        }
    }

    private void saveTags(Long noteId, List<Long> tagIds) {
        noteTagMapper.delete(new LambdaQueryWrapper<NoteTag>().eq(NoteTag::getNoteId, noteId));
        if (tagIds == null) {
            return;
        }
        tagIds.stream().distinct().forEach(tagId -> {
            NoteTag relation = new NoteTag();
            relation.setNoteId(noteId);
            relation.setTagId(tagId);
            noteTagMapper.insert(relation);
        });
    }

    private NoteVO toVO(Note note) {
        NoteVO vo = NoteVO.from(note);
        if (note.getCategoryId() != null) {
            Category category = categoryMapper.selectById(note.getCategoryId());
            vo.setCategoryName(category == null ? null : category.getName());
        }
        List<Long> tagIds = noteTagMapper.selectList(new LambdaQueryWrapper<NoteTag>().eq(NoteTag::getNoteId, note.getId()))
                .stream().map(NoteTag::getTagId).toList();
        if (!tagIds.isEmpty()) {
            vo.setTagNames(tagMapper.selectBatchIds(tagIds).stream().map(Tag::getName).toList());
        } else {
            vo.setTagNames(Collections.emptyList());
        }
        return vo;
    }

    private int nullToZero(Integer value) {
        return value == null ? 0 : value;
    }
}
