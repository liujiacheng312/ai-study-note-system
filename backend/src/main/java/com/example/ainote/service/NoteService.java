package com.example.ainote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ainote.common.PageResult;
import com.example.ainote.dto.NoteQueryRequest;
import com.example.ainote.dto.NoteRequest;
import com.example.ainote.entity.Note;
import com.example.ainote.vo.NoteVO;

public interface NoteService extends IService<Note> {
    NoteVO create(NoteRequest request);

    NoteVO updateNote(Long id, NoteRequest request);

    void deleteNote(Long id);

    NoteVO detail(Long id);

    PageResult<NoteVO> myPage(NoteQueryRequest query);

    PageResult<NoteVO> publicPage(NoteQueryRequest query);

    NoteVO submitAi(Long id);

    NoteVO publish(Long id);

    NoteVO archive(Long id);
}
