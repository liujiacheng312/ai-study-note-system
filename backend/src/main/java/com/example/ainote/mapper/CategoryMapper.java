package com.example.ainote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ainote.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
