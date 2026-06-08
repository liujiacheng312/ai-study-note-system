package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ainote.entity.AiUsageLog;
import com.example.ainote.mapper.AiUsageLogMapper;
import com.example.ainote.service.AiUsageLogService;
import org.springframework.stereotype.Service;

@Service
public class AiUsageLogServiceImpl extends ServiceImpl<AiUsageLogMapper, AiUsageLog> implements AiUsageLogService {
}
