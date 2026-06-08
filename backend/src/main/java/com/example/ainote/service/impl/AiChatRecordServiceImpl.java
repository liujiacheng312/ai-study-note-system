package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ainote.entity.AiChatRecord;
import com.example.ainote.mapper.AiChatRecordMapper;
import com.example.ainote.service.AiChatRecordService;
import org.springframework.stereotype.Service;

@Service
public class AiChatRecordServiceImpl extends ServiceImpl<AiChatRecordMapper, AiChatRecord> implements AiChatRecordService {
}
