package com.example.ainote.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ainote.common.BusinessException;
import com.example.ainote.dto.LoginRequest;
import com.example.ainote.dto.PasswordUpdateRequest;
import com.example.ainote.dto.RegisterRequest;
import com.example.ainote.dto.UserProfileUpdateRequest;
import com.example.ainote.entity.LoginLog;
import com.example.ainote.entity.SysUser;
import com.example.ainote.mapper.LoginLogMapper;
import com.example.ainote.mapper.SysUserMapper;
import com.example.ainote.service.SysUserService;
import com.example.ainote.util.JwtUtil;
import com.example.ainote.util.SecurityUtils;
import com.example.ainote.vo.LoginVO;
import com.example.ainote.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LoginLogMapper loginLogMapper;

    @Override
    @Transactional
    public UserVO register(RegisterRequest request) {
        Long count = lambdaQuery().eq(SysUser::getUsername, request.getUsername()).count();
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname() : request.getUsername());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        user.setRole("USER");
        save(user);
        return UserVO.from(user);
    }

    @Override
    public LoginVO login(LoginRequest request, String ip, String userAgent) {
        SysUser user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()));
        LoginLog log = new LoginLog();
        log.setUsername(request.getUsername());
        log.setIp(ip);
        log.setUserAgent(userAgent);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.setStatus("FAIL");
            log.setMessage("用户名或密码错误");
            loginLogMapper.insert(log);
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() == 0) {
            log.setUserId(user.getId());
            log.setStatus("FAIL");
            log.setMessage("账号已被禁用");
            loginLogMapper.insert(log);
            throw new BusinessException("账号已被禁用");
        }
        log.setUserId(user.getId());
        log.setStatus("SUCCESS");
        log.setMessage("登录成功");
        loginLogMapper.insert(log);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginVO(token, UserVO.from(user));
    }

    @Override
    public UserVO profile() {
        return UserVO.from(getById(SecurityUtils.getCurrentUserId()));
    }

    @Override
    public UserVO updateProfile(UserProfileUpdateRequest request) {
        SysUser user = getById(SecurityUtils.getCurrentUserId());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setAvatar(request.getAvatar());
        updateById(user);
        return UserVO.from(user);
    }

    @Override
    public void updatePassword(PasswordUpdateRequest request) {
        SysUser user = getById(SecurityUtils.getCurrentUserId());
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        updateById(user);
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if ("ADMIN".equals(user.getRole())) {
            throw new BusinessException("不能禁用管理员账号");
        }
        user.setStatus(status);
        updateById(user);
    }

    @Override
    public void resetPassword(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        updateById(user);
    }
}
