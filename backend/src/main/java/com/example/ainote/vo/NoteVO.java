package com.example.ainote.vo;

import com.example.ainote.entity.Note;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class NoteVO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String content;
    private String summary;
    private String aiKeyPoints;
    private String aiExamPoints;
    private String aiSuggestion;
    private String status;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> tagNames = new ArrayList<>();

    public static NoteVO from(Note note) {
        NoteVO vo = new NoteVO();
        vo.setId(note.getId());
        vo.setUserId(note.getUserId());
        vo.setCategoryId(note.getCategoryId());
        vo.setTitle(note.getTitle());
        vo.setContent(note.getContent());
        vo.setSummary(note.getSummary());
        vo.setAiKeyPoints(note.getAiKeyPoints());
        vo.setAiExamPoints(note.getAiExamPoints());
        vo.setAiSuggestion(note.getAiSuggestion());
        vo.setStatus(note.getStatus());
        vo.setViewCount(note.getViewCount());
        vo.setLikeCount(note.getLikeCount());
        vo.setFavoriteCount(note.getFavoriteCount());
        vo.setCommentCount(note.getCommentCount());
        vo.setCreateTime(note.getCreateTime());
        vo.setUpdateTime(note.getUpdateTime());
        return vo;
    }
}
