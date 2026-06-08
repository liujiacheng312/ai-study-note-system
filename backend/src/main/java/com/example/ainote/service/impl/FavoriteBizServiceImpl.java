package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ainote.common.BusinessException;
import com.example.ainote.common.PageResult;
import com.example.ainote.entity.Favorite;
import com.example.ainote.entity.Note;
import com.example.ainote.enums.NoteStatus;
import com.example.ainote.mapper.FavoriteMapper;
import com.example.ainote.mapper.NoteMapper;
import com.example.ainote.service.FavoriteBizService;
import com.example.ainote.service.NoteService;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.NoteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteBizServiceImpl implements FavoriteBizService {
    private final FavoriteMapper favoriteMapper;
    private final NoteMapper noteMapper;
    private final NoteService noteService;

    @Override
    @Transactional
    public void add(Long noteId) {
        Long userId = SecurityUtils.getCurrentUserId();
        Note note = noteMapper.selectById(noteId);
        if (note == null || !NoteStatus.PUBLISHED.name().equals(note.getStatus())) {
            throw new BusinessException("只能收藏已发布笔记");
        }
        Long count = favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId).eq(Favorite::getNoteId, noteId));
        if (count == 0) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setNoteId(noteId);
            favoriteMapper.insert(favorite);
            note.setFavoriteCount((note.getFavoriteCount() == null ? 0 : note.getFavoriteCount()) + 1);
            noteMapper.updateById(note);
        }
    }

    @Override
    @Transactional
    public void remove(Long noteId) {
        Long userId = SecurityUtils.getCurrentUserId();
        favoriteMapper.delete(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId).eq(Favorite::getNoteId, noteId));
    }

    @Override
    public Boolean check(Long noteId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId).eq(Favorite::getNoteId, noteId)) > 0;
    }

    @Override
    public PageResult<NoteVO> myPage(Long pageNo, Long pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<Favorite> page = favoriteMapper.selectPage(new Page<>(pageNo, pageSize), new LambdaQueryWrapper<Favorite>().eq(Favorite::getUserId, userId).orderByDesc(Favorite::getCreateTime));
        List<NoteVO> records = page.getRecords().stream().map(Favorite::getNoteId).map(noteService::detail).toList();
        return new PageResult<>(page.getTotal(), pageNo, pageSize, records);
    }
}
