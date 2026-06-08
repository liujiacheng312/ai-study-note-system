package com.example.ainote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ainote.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
