package com.example.ainote.config;

import com.example.ainote.entity.OperationLog;
import com.example.ainote.mapper.OperationLogMapper;
import com.example.ainote.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {
    private final OperationLogMapper operationLogMapper;

    @AfterReturning("execution(* com.example.ainote.controller.AdminController.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        save(joinPoint, "SUCCESS");
    }

    @AfterThrowing("execution(* com.example.ainote.controller.AdminController.*(..))")
    public void afterThrowing(JoinPoint joinPoint) {
        save(joinPoint, "FAIL");
    }

    private void save(JoinPoint joinPoint, String status) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return;
        }
        HttpServletRequest request = attrs.getRequest();
        OperationLog log = new OperationLog();
        log.setUserId(resolveUserId());
        log.setOperation(joinPoint.getSignature().getName());
        log.setMethod(request.getMethod());
        log.setRequestUri(request.getRequestURI());
        log.setIp(request.getRemoteAddr());
        log.setStatus(status);
        operationLogMapper.insert(log);
    }

    private Long resolveUserId() {
        try {
            return SecurityUtils.getCurrentUserId();
        } catch (Exception ex) {
            return null;
        }
    }
}
