package com.example.ainote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ainote.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
