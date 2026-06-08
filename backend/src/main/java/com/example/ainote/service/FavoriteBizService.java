package com.example.ainote.service;

import com.example.ainote.common.PageResult;
import com.example.ainote.vo.NoteVO;

public interface FavoriteBizService {
    void add(Long noteId);

    void remove(Long noteId);

    Boolean check(Long noteId);

    PageResult<NoteVO> myPage(Long pageNo, Long pageSize);
}
