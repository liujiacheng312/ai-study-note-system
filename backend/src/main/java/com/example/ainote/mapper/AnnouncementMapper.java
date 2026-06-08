package com.example.ainote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ainote.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
