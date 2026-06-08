package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ainote.entity.AiUsageLog;
import com.example.ainote.entity.Category;
import com.example.ainote.entity.Favorite;
import com.example.ainote.entity.Note;
import com.example.ainote.entity.SysUser;
import com.example.ainote.enums.NoteStatus;
import com.example.ainote.mapper.AiUsageLogMapper;
import com.example.ainote.mapper.CategoryMapper;
import com.example.ainote.mapper.FavoriteMapper;
import com.example.ainote.mapper.NoteMapper;
import com.example.ainote.mapper.SysUserMapper;
import com.example.ainote.service.StatisticsService;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.StatisticsOverviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final NoteMapper noteMapper;
    private final FavoriteMapper favoriteMapper;
    private final AiUsageLogMapper aiUsageLogMapper;
    private final SysUserMapper sysUserMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public StatisticsOverviewVO overview(boolean admin) {
        Long userId = admin ? null : SecurityUtils.getCurrentUserId();
        LambdaQueryWrapper<Note> noteWrapper = new LambdaQueryWrapper<Note>().ne(Note::getStatus, NoteStatus.DELETED.name());
        LambdaQueryWrapper<Note> weekWrapper = new LambdaQueryWrapper<Note>().ge(Note::getCreateTime, LocalDateTime.now().minusDays(7));
        LambdaQueryWrapper<Favorite> favWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<AiUsageLog> aiWrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            noteWrapper.eq(Note::getUserId, userId);
            weekWrapper.eq(Note::getUserId, userId);
            favWrapper.eq(Favorite::getUserId, userId);
            aiWrapper.eq(AiUsageLog::getUserId, userId);
        }
        Long userTotal = admin ? sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>()) : 1L;
        return new StatisticsOverviewVO(
                noteMapper.selectCount(noteWrapper),
                noteMapper.selectCount(weekWrapper),
                favoriteMapper.selectCount(favWrapper),
                aiUsageLogMapper.selectCount(aiWrapper),
                userTotal);
    }

    @Override
    public List<Map<String, Object>> category() {
        Long userId = SecurityUtils.isAdmin() ? null : SecurityUtils.getCurrentUserId();
        return categoryMapper.selectList(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSortOrder)).stream().map(category -> {
            LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<Note>().eq(Note::getCategoryId, category.getId()).ne(Note::getStatus, NoteStatus.DELETED.name());
            if (userId != null) {
                wrapper.eq(Note::getUserId, userId);
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", category.getName());
            item.put("value", noteMapper.selectCount(wrapper));
            return item;
        }).toList();
    }

    @Override
    public List<Map<String, Object>> monthly() {
        Long userId = SecurityUtils.isAdmin() ? null : SecurityUtils.getCurrentUserId();
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 5; i >= 0; i--) {
            LocalDateTime start = LocalDateTime.now().minusMonths(i).withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime end = start.plusMonths(1);
            LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<Note>().ge(Note::getCreateTime, start).lt(Note::getCreateTime, end);
            if (userId != null) {
                wrapper.eq(Note::getUserId, userId);
            }
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("month", start.format(formatter));
            item.put("count", noteMapper.selectCount(wrapper));
            list.add(item);
        }
        return list;
    }
}
