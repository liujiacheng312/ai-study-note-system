package com.example.ainote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ainote.entity.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}
